package com.thistech.vex.harness.mockserver.handler.websocket;

import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by modoso on 15/3/4.
 */
@Component
@ConfigurationProperties(
        prefix = "servers.web-socket",
        ignoreUnknownFields = false
        // ,locations = "classpath:conf/mock-server.yaml"
)
public class WebSocketConfiguration extends HandlerConfiguration {
}