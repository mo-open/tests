package org.akka.essentials.zeromq.java.example3.client;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.ByteString;
import akka.zeromq.Connect;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;
import scala.concurrent.duration.Duration;

public class ClientActor extends UntypedActor {
    public static final Object TICK = "TICK";
    int count = 0;
    Cancellable cancellable;
    ActorRef reqSocket = ZeroMQExtension.get(getContext().system())
            .newReqSocket(
                    new SocketOption[]{new Connect("tcp://127.0.0.1:1237"),
                            new Listener(getSelf())});
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void preStart() {
        cancellable = getContext()
                .system()
                .scheduler()
                .schedule(Duration.apply(1, "second"),
                        Duration.apply(1, "second"),
                        getSelf(),
                        TICK, getContext().dispatcher(), ActorRef.noSender());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message.equals(TICK)) {
            // send a message to the replier system
            reqSocket.tell(ZMQMessage.withFrames(ByteString.fromString("Hi there! ("
                    + getContext().self().hashCode() + ")->")),getSelf());
            count++;
            if (count == 10)
                cancellable.cancel();
        } else if (message instanceof ZMQMessage) {
            ZMQMessage m = (ZMQMessage) message;
            String mesg = m.frame(0).toString();

            log.info("Received msg! {}", mesg);
        }
    }
}
