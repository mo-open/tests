package com.thistech.vex.harness.mockserver.handler.contentrouter;

import com.thistech.vex.harness.mockserver.AConfiguration;
import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import com.thistech.vex.harness.mockserver.ServerHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;

/**
 * Created by modongsong on 2014/10/13.
 */
@Component
@ConditionalOnProperty(prefix = "servers.content-router", name = "enable", havingValue = "true")
@ChannelHandler.Sharable
public class ContentRouterHandler extends ServerHandler {
    private List<String> ecUris = new ArrayList<>();
    private static Random random = new Random();

    @Autowired
    private ContentRouterConfiguration configuration;

    @Override
    protected HandlerConfiguration configuration() {
        return this.configuration;
    }

    @PostConstruct
    protected void init() {
        super.init();
        List<ContentRouterConfiguration.EdgeCache> edgeCaches = this.configuration.getEdgeCaches();
        if (edgeCaches != null) {
            for (ContentRouterConfiguration.EdgeCache edgeCache : edgeCaches) {
                this.ecUris.addAll(edgeCache.uris());
            }
        }
    }

    @Override
    protected String handle(ChannelHandlerContext ctx, FullHttpRequest msg) {
        if (this.configuration.isUseLocation()) return null;
        return this.generateECUri(msg);
    }

    private String generateECUri(FullHttpRequest msg) {
        return this.ecUris.get(random.nextInt(this.ecUris.size())) + msg.getUri();
    }

    @Override
    protected void beforeWriteResponse(ChannelHandlerContext ctx,
                                       FullHttpRequest msg, FullHttpResponse response) {
        if (!this.configuration.isUseLocation()) return;
        response.headers().set(LOCATION, this.generateECUri(msg));
    }
}
