package com.thistech.vex.harness.mockserver.handler.callback;

import com.thistech.vex.harness.mockserver.AConfiguration;
import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by modoso on 15/2/26.
 */
@Component
@ConfigurationProperties(
        prefix = "servers.cacheMiss-callback",
        ignoreUnknownFields = false
        // ,locations = "classpath:conf/mock-server.yaml"
)
public class CallbackConfiguration extends HandlerConfiguration {
}
