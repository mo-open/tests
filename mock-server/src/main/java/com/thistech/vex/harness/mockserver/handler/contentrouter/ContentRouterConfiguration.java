package com.thistech.vex.harness.mockserver.handler.contentrouter;

import com.thistech.vex.harness.mockserver.AConfiguration;
import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modongsong on 2014/10/13.
 */
@Component
@ConfigurationProperties(
        prefix = "servers.content-router",
        ignoreUnknownFields = false
        //,locations = "classpath:conf/mock-server.yaml"
)
public class ContentRouterConfiguration extends HandlerConfiguration {
    private List<EdgeCache> edgeCaches = new ArrayList();
    private boolean useLocation = true;

    public boolean isUseLocation() {
        return useLocation;
    }

    public void setUseLocation(boolean useLocation) {
        this.useLocation = useLocation;
    }

    public List<EdgeCache> getEdgeCaches() {
        return edgeCaches;
    }

    public void setEdgeCaches(List<EdgeCache> edgeCaches) {
        this.edgeCaches = edgeCaches;
    }

    public static class EdgeCache {
        private String namePrefix;
        private int rangeFrom;
        private int rangeTo;
        private int port = -1;

        public String getNamePrefix() {
            return namePrefix;
        }

        public void setNamePrefix(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        public int getRangeFrom() {
            return rangeFrom;
        }

        public void setRangeFrom(int rangeFrom) {
            this.rangeFrom = rangeFrom;
        }

        public int getRangeTo() {
            return rangeTo;
        }

        public void setRangeTo(int rangeTo) {
            this.rangeTo = rangeTo;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public List<String> uris() {
            if (StringUtils.isEmpty(this.namePrefix)) return null;
            List<String> uris = new ArrayList<>();
            if (this.rangeFrom < this.rangeTo) {
                String uri = this.namePrefix;
                if (this.port > 0) uri = uri + ":" + port;
                uris.add(uri);
                return uris;
            }
            for (int i = this.rangeFrom; i <= this.rangeTo; i++) {
                String uri = this.namePrefix + i;
                if (this.port > 0) uri = uri + ":" + port;
                uris.add(uri);
            }
            return uris;
        }
    }
}
