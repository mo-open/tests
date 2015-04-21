package com.agiledeveloper.pcj

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Actors
import akka.actor.ActorRegistry

//START:CASECLASSES
case object RequestAFile
case class FileSize(size : Long)
case class FileToProcess(fileName : String)
//END:CASECLASSES

//START:FILEPROCESSOR
class FileProcessor(val sizeCollector : ActorRef) extends Actor {
  override def preStart = registerToGetFile
  
  def registerToGetFile = { sizeCollector ! RequestAFile }
  
  def receive = {
    case FileToProcess(fileName) =>
      val file = new java.io.File(fileName)

      var size = 0L
      if(file.isFile()) {
        size = file.length()
      } else {
        val children = file.listFiles()
        if (children != null)
          for(child <- children)
            if (child.isFile())
              size += child.length()
            else
              sizeCollector ! FileToProcess(child.getPath())
      }
      
      sizeCollector ! FileSize(size)
      registerToGetFile    
  }
}
//END:FILEPROCESSOR

//START:SIZECOLLECTOR
class SizeCollector extends Actor {
  var toProcessFileNames = List.empty[String]
  var fileProcessors = List.empty[ActorRef]
  var pendingNumberOfFilesToVisit = 0L
  var totalSize = 0L
  val start = System.nanoTime()

  def sendAFileToProcess() : Unit = {
    if(!toProcessFileNames.isEmpty && !fileProcessors.isEmpty) {
      fileProcessors.head ! FileToProcess(toProcessFileNames.head)
      fileProcessors = fileProcessors.tail
      toProcessFileNames = toProcessFileNames.tail      
    }
  }    
  def receive = {
    case RequestAFile =>
      fileProcessors = self.getSender().get :: fileProcessors
      sendAFileToProcess()     
    case FileToProcess(fileName) =>
      toProcessFileNames = fileName :: toProcessFileNames
      pendingNumberOfFilesToVisit += 1
      sendAFileToProcess()
    case FileSize(size) =>
      totalSize += size
      pendingNumberOfFilesToVisit -= 1
      if(pendingNumberOfFilesToVisit == 0) {
        val end = System.nanoTime()
        println("Total size is " + totalSize)
        println("Time taken is " + (end - start)/1.0e9)        
        Actors.registry.shutdownAll
      }
  }
}
//END:SIZECOLLECTOR

//START:MAIN
object ConcurrentFileSizeWAkka {
  def main(args : Array[String]) : Unit = {
    val sizeCollector = Actor.actorOf[SizeCollector].start()
    
    sizeCollector ! FileToProcess(args(0))
    
    for(i <- 1 to 100) 
      Actor.actorOf(new FileProcessor(sizeCollector)).start()
  }  
}
//END:MAIN
