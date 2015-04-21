#---
# Excerpted from "Programming Concurrency on the JVM",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
#---
class RequestAFile; end

class FileSize
  attr_reader :size
  def initialize(size)
    @size = size
  end
end

class FileToProcess
  attr_reader :file_name
  def initialize(file_name)
    @file_name = file_name
  end
end


require 'java'
java_import java.lang.System
java_import 'akka.actor.ActorRegistry'
java_import 'akka.actor.Actors'
java_import 'akka.actor.UntypedActor'

class FileProcessor < UntypedActor
  attr_accessor :size_collector
  
  def initialize(size_collector)
    super()
    @size_collector = size_collector
  end
  
  def preStart
    register_to_get_file
  end
  
  def register_to_get_file
    @size_collector.send_one_way(RequestAFile.new, context)
  end
  
  def onReceive(fileToProcess)
    file = java.io.File.new(fileToProcess.file_name)
    size = 0
    if !file.isDirectory()
      size = file.length()
    else
      children = file.listFiles()
      if children != nil
        children.each do |child|
          if child.isFile()
            size += child.length()
          else
            @size_collector.send_one_way(FileToProcess.new(child.getPath()))
          end
        end
      end
    end    
    @size_collector.send_one_way(FileSize.new(size))
    register_to_get_file
  end
end

class SizeCollector < UntypedActor
  def self.create(*args)
    self.new(*args)
  end

  def initialize
    @to_process_file_names = []
    @file_processors = []
    @fetch_size_future = nil
    @pending_number_of_files_to_visit = 0
    @total_size = 0
    @start_time = System.nano_time
  end
  
  def send_a_file_to_process
    if !@to_process_file_names.empty? && !@file_processors.empty? 
      @file_processors.first.send_one_way(
        FileToProcess.new(@to_process_file_names.first))
      @file_processors = @file_processors.drop(1)
      @to_process_file_names = @to_process_file_names.drop(1)
    end
  end
   
  def onReceive(message)
    case message
      when RequestAFile
        @file_processors << context.sender.get
        send_a_file_to_process
      
      when FileToProcess
        @to_process_file_names << message.file_name
        @pending_number_of_files_to_visit += 1
        send_a_file_to_process

      when FileSize
        @total_size += message.size
        @pending_number_of_files_to_visit -= 1
        if @pending_number_of_files_to_visit == 0
          end_time = System.nano_time()
          puts "Total size is #{@total_size}"
          puts "Time taken is #{(end_time - @start_time)/1.0e9}"
          Actors.registry().shutdownAll()
        end
      end
  end
end

size_collector = Actors.actor_of(SizeCollector).start()
size_collector.send_one_way(FileToProcess.new(ARGV[0]))

100.times do
  Actors.actor_of(lambda { FileProcessor.new(size_collector) }).start
end

