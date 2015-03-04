package org.akka.essentials.zeromq.java.example1;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.util.ByteString;
import akka.zeromq.Bind;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;
import scala.concurrent.duration.Duration;

public class PublisherActor extends UntypedActor {
    public static final Object TICK = "TICK";
    int count = 0;
    Cancellable cancellable;
    ActorRef pubSocket = ZeroMQExtension.get(getContext().system())
            .newPubSocket(new Bind("tcp://127.0.0.1:1237"));

    @Override
    public void preStart() {
        cancellable = getContext()
                .system()
                .scheduler()
                .schedule(Duration.apply(1, "second"),
                        Duration.apply(1, "second"),
                        getSelf(),
                        TICK,
                        getContext().dispatcher(),
                        ActorRef.noSender());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message.equals(TICK)) {
            pubSocket.tell(ZMQMessage.withFrames(ByteString.fromString("someTopic"),
                    ByteString.fromString("This is the workload " + ++count)), getSelf());

            if (count == 10)
                cancellable.cancel();
        }
    }
}
