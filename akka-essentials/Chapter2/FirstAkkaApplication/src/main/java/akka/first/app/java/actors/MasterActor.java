package akka.first.app.java.actors;

import akka.actor.*;
import akka.first.app.java.messages.Result;
import akka.japi.Creator;

public class MasterActor extends UntypedActor {

    private ActorRef aggregateActor = getContext().actorOf(
            Props.create(AggregateActor.class), "aggregate");

    static class ReduceActorCreator implements Creator<ReduceActor> {
        ActorRef actorRef;

        public ReduceActorCreator(ActorRef actorRef) {
            this.actorRef = actorRef;
        }

        @Override
        public ReduceActor create() throws Exception {
            return new ReduceActor(this.actorRef);
        }
    }

    static class MapActorCreator implements Creator<MapActor> {
        ActorRef actorRef;

        public MapActorCreator(ActorRef actorRef) {
            this.actorRef = actorRef;
        }

        @Override
        public MapActor create() throws Exception {
            return new MapActor(this.actorRef);
        }
    }

    private ActorRef reduceActor = getContext().actorOf(
            Props.create(new ReduceActorCreator(aggregateActor)));

    private ActorRef mapActor = getContext().actorOf(
            Props.create(new MapActorCreator(reduceActor)));

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            mapActor.tell(message, getSelf());
        } else if (message instanceof Result) {
            aggregateActor.tell(message, getSelf());
        } else
            unhandled(message);
    }
}
