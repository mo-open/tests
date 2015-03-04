package com.thistech.vex.harness.mockserver.handler.director;

import com.thistech.vex.harness.mockserver.AConfiguration;
import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import com.thistech.vex.harness.mockserver.ServerHandler;
import com.thistech.vex.harness.mockserver.handler.ads.AdsConfiguration;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Created by modongsong on 2014/10/13.
 */
@Component
@ConditionalOnProperty(prefix = "servers.director", name = "enable", havingValue = "true")
@ChannelHandler.Sharable
public class DirectorHandler extends ServerHandler {
    @Autowired
    private DirectorConfiguration configuration;

    @Override
    protected HandlerConfiguration configuration() {
        return this.configuration;
    }

    @Override
    protected String handle(ChannelHandlerContext ctx, FullHttpRequest msg) {
        return "Director Response";
    }
}
