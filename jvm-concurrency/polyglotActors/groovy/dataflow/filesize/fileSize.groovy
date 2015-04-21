/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
import groovyx.gpars.dataflow.DataFlowQueue
import groovyx.gpars.group.DefaultPGroup

class FileSize {
  private final pendingFiles = new DataFlowQueue()
  private final sizes = new DataFlowQueue()
  private final group = new DefaultPGroup()

  def findSize(File file) {
    def size = 0
    if(!file.isDirectory()) 
      size = file.length()
    else {
      def children = file.listFiles()
      if (children != null) {
        children.each { child ->
          if(child.isFile())
            size += child.length()
          else {
            pendingFiles << 1
            group.task { findSize(child) }
          }
        }
      }
    }
    
    pendingFiles << -1
    sizes << size
  }
  
  def findTotalFileSize(File file) {
    pendingFiles << 1
    group.task { findSize(file) }

    int filesToVisit = 0
    long totalSize = 0
    while(true) {
      totalSize += sizes.val
      if(!(filesToVisit += (pendingFiles.val + pendingFiles.val))) break
    }
    
    totalSize
  }
}

start = System.nanoTime()
totalSize = new FileSize().findTotalFileSize(new File(args[0]))
println("Total size $totalSize")
println("Time taken ${(System.nanoTime() - start) / 1.0e9}")
