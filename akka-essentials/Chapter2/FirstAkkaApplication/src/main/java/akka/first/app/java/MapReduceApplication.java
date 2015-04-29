package akka.first.app.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.first.app.scala.actors.MasterActor;
import akka.first.app.java.messages.Result;

public class MapReduceApplication {

    public static void main(String[] args) throws Exception {

        ActorSystem _system = ActorSystem.create("MapReduceApp");

        ActorRef master = _system.actorOf(Props.create(MasterActor.class), "master");

        master.tell("The quick brown fox tried to jump over the lazy dog and fell on the dog", master);
        master.tell("Dog is man's best friend", master);
        master.tell("Dog and Fox belong to the same family", master);

        Thread.sleep(500);

        master.tell(new Result(), master);

        Thread.sleep(500);

        _system.shutdown();
    }
}
