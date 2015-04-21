/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
import groovyx.gpars.actor.DynamicDispatchActor
import groovyx.gpars.actor.DefaultActor
import java.util.concurrent.TimeUnit

@Immutable class RequestAFile {}
@Immutable class FileSize { long size }
@Immutable class FileToProcess { String fileName }

class FileProcessor extends DefaultActor {
  def sizeCollector
  
  public FileProcessor(theSizeCollector) { sizeCollector = theSizeCollector }
  
  void afterStart() { registerToGetFile() }  
  
  void registerToGetFile() { sizeCollector << new RequestAFile() }
  
  void act()  {
    loop {
      react { message ->
        def file = new File(message.fileName)
        def size = 0
        if(!file.isDirectory()) 
          size = file.length()
        else {
          def children = file.listFiles()
          if (children != null) {
            children.each { child ->
              if(child.isFile())
                size += child.length()
              else
                sizeCollector << new FileToProcess(child.path)
            }
          }
        }
        sizeCollector << new FileSize(size)
        registerToGetFile()  
      }
    }
  }
}

class SizeCollector extends DynamicDispatchActor {
  def toProcessFileNames = []
  def idleFileProcessors = []
  def pendingNumberOfFilesToVisit = 0
  def totalSize = 0L
  final def start = System.nanoTime()
  
  def sendAFileToProcess() {
    if(toProcessFileNames && idleFileProcessors) {
      idleFileProcessors.first() << 
        new FileToProcess(toProcessFileNames.first())
      idleFileProcessors = idleFileProcessors.tail()
      toProcessFileNames = toProcessFileNames.tail()      
    }
  }

  void onMessage(RequestAFile message) {
    idleFileProcessors.add(sender)
    sendAFileToProcess()
  }
    
  void onMessage(FileToProcess message) {
    toProcessFileNames.add(message.fileName)
    pendingNumberOfFilesToVisit += 1
    sendAFileToProcess()
  }
  
  void onMessage(FileSize message) {
    totalSize += message.size
    pendingNumberOfFilesToVisit -= 1
    if(pendingNumberOfFilesToVisit == 0) {
      def end = System.nanoTime()
      println "Total size is $totalSize"
      println "Time taken is ${(end - start)/1.0e9}"
      terminate()
    }
  }
}

sizeCollector = new SizeCollector().start()
sizeCollector << new FileToProcess(args[0])

100.times { new FileProcessor(sizeCollector).start() }

sizeCollector.join(100, java.util.concurrent.TimeUnit.SECONDS)
