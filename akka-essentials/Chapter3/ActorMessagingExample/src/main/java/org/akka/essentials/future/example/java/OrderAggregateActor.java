package org.akka.essentials.future.example.java;

import org.akka.essentials.future.example.messages.OrderHistory;

import akka.actor.UntypedActor;

public class OrderAggregateActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof OrderHistory) {
            OrderHistory orderHistory = (OrderHistory) message;
            System.out.println("Order History -> " + orderHistory.getOrder()
                    + "\n" + orderHistory.getAddress());
        }
    }
}
