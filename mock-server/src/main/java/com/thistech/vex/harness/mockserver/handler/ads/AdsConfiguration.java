package com.thistech.vex.harness.mockserver.handler.ads;

import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by modoso on 15/2/26.
 */
@Component
@ConfigurationProperties(
        prefix = "servers.ads",
        ignoreUnknownFields = false
     // ,locations = "classpath:conf/mock-server.yaml"
)
public class AdsConfiguration extends HandlerConfiguration {
}
