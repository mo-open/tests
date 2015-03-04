package com.thistech.vex.harness.mockserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "common",
        ignoreUnknownFields = false
        // ,locations = "classpath:conf/mock-server.yaml"
)
public class CommonConfig {
    private int acceptors;
    private int workers;

    public int getAcceptors() {
        return acceptors;
    }

    public void setAcceptors(int acceptors) {
        this.acceptors = acceptors;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }
}
