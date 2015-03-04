package com.thistech.vex.harness.mockserver.metrics;

import org.springframework.stereotype.Component;

import com.codahale.metrics.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.jvm.*;

/**
 * Created by modongsong on 2014/10/13.
 */
@Component
public class Metrics {
    private MetricRegistry generalMetrics;

    @PostConstruct
    protected void start() {
        generalMetrics = new MetricRegistry();
        startReport();
    }

    private void startReport() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(generalMetrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(30, TimeUnit.SECONDS);
    }
}
