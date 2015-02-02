package org.mds.tests.scalatest

/**
 *
 * @author Dongsong
 */
trait Logger {
    def log(msg: String)
}

trait ConsoleLogger extends Logger {
    //here the abstract key must be
    abstract override def log(msg: String) {
        println("Console: " + msg)
    }
}

trait TimeLogger extends Logger {
    abstract override def log(msg: String) {
        super.log(new java.util.Date() + "--" + msg)
    }
}

trait Logger1 {
    def log(msg: String) {
        println(msg)
    }
}

trait ConsoleLogger1 extends Logger1 {
    override def log(msg: String) {
        super.log("Console: " + msg)
    }
}

trait TimeLogger1 extends Logger1 {
    override def log(msg: String) {
        super.log(new java.util.Date() + "--" + msg)
    }
}

class TraitTest extends Object with Logger {
    def log(msg: String) {

    }

    def go() {
        log("test")
    }
}

class TraitTest1 extends Object with Logger1 {
    def go() {
        log("test")
    }
}

object App {
    def main(args: Array[String]) {
        val obj = new TraitTest with ConsoleLogger with TimeLogger
        //out example: Console:Sat Jan 31 13:35:49 CST 2015--test
        obj.go()

        val obj1 = new TraitTest1 with ConsoleLogger1 with TimeLogger1
        //out example: Console:Sat Jan 31 13:35:49 CST 2015--test
        obj1.go()
    }
}
