package org.kafecho.learning.jozispray

import scala.xml.NodeSeq

object TypeclassPattern extends App{

  case class Person(firstname: String, surname: String)
  
  trait XMLWriter[T]{
    def write(value: T) : NodeSeq
  }
 
  object XMLWriter{
    implicit object PersonWriter extends XMLWriter[Person]{
      def write(value: Person) = <person><firstname>{value.firstname}</firstname><surname>{value.surname}</surname></person>
    }
  }
  
  object Marshaller{
    def marshall[T](what: T)(implicit writer: XMLWriter[T]) : NodeSeq = writer.write(what)
  }
  
  import XMLWriter._

  println (Marshaller.marshall(Person("Guillaume","Belrose")))
}