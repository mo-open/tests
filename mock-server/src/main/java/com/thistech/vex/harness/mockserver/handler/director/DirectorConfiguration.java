package com.thistech.vex.harness.mockserver.handler.director;

import com.thistech.vex.harness.mockserver.AConfiguration;
import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by modoso on 15/2/26.
 */
@Component
@ConfigurationProperties(
        prefix = "servers.director",
        ignoreUnknownFields = false
        // ,locations = "classpath:conf/mock-server.yaml"
)
public class DirectorConfiguration extends HandlerConfiguration {
}
