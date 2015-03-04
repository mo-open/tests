package com.thistech.vex.harness.mockserver.handler.origin;

import com.thistech.vex.harness.mockserver.AConfiguration;
import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by modongsong on 2014/10/13.
 */
@Component
@ConfigurationProperties(
        prefix = "servers.origin",
        ignoreUnknownFields = false
        //,locations = "classpath:conf/mock-server.yaml"
)
public class OriginConfiguration extends HandlerConfiguration {
    private String responseFile;

    public String getResponseFile() {
        return responseFile;
    }

    public void setResponseFile(String responseFile) {
        this.responseFile = responseFile;
    }
}
