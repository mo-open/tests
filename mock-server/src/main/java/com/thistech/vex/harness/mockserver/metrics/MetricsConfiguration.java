package com.thistech.vex.harness.mockserver.metrics;

import com.thistech.vex.harness.mockserver.AConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by modongsong on 2014/10/13.
 */
@Component
@ConfigurationProperties(
        prefix = "metrics",
        ignoreUnknownFields = true
        //,locations = "classpath:conf/mock-server.yaml"
)
public class MetricsConfiguration extends AConfiguration {

}
