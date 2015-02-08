package org.akka.essentials.stm.java.stockticker.example;

import akka.japi.Function;
import scala.Function1;

public class StockUpdater implements Runnable {

    private int countDown = 5;
    private Stock stock;

    public StockUpdater(Stock inStock) {
        stock = inStock;
    }

    public void run() {
        while (countDown > 0) {
            try {
                Thread.sleep(75);
            } catch (InterruptedException e) {
            }
            String x = Thread.currentThread().getName();
            stock.getPrice().send(new Function1<Float, Float>() {
                public Float apply(Float i) {
                    return i + 10;
                }

                @Override
                public <A> Function1<A, Float> compose(Function1<A, Float> g) {
                    return null;
                }

                @Override
                public <A> Function1<Float, A> andThen(Function1<Float, A> g) {
                    return null;
                }
            });
            System.out.println("Quote update by thread (" + x
                    + "), current price " + stock.getPrice().get());
            countDown = countDown - 1;
        }
    }
}
