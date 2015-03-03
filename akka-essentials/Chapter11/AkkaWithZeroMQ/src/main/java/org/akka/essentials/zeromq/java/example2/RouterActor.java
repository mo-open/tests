package org.akka.essentials.zeromq.java.example2;

import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.ByteString;
import akka.zeromq.Bind;
import akka.zeromq.HighWatermark;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;
import scala.concurrent.duration.Duration;

public class RouterActor extends UntypedActor {
    public static final Object TICK = "TICK";

    Random random = new Random(3);
    int count = 0;
    Cancellable cancellable;

    ActorRef routerSocket = ZeroMQExtension.get(getContext().system())
            .newRouterSocket(
                    new SocketOption[]{new Listener(getSelf()),
                            new Bind("tcp://127.0.0.1:1237"),
                            new HighWatermark(50000)});
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

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

            if (random.nextBoolean() == true) {
                routerSocket.tell(ZMQMessage.withFrames(ByteString.fromString("A"), ByteString.fromString(
                        "This is the workload for A")), getSelf());
            } else {
                routerSocket.tell(ZMQMessage.withFrames(ByteString.fromString("B"), ByteString.fromString(
                        "This is the workload for B")), getSelf());

            }
            count++;
            if (count == 10)
                cancellable.cancel();

        } else if (message instanceof ZMQMessage) {
            ZMQMessage m = (ZMQMessage) message;
            String replier = m.frame(0).utf8String();
            String msg = m.frame(1).utf8String();
            log.info("Received message from {} with mesg -> {}", replier, msg);
        }

    }

}
