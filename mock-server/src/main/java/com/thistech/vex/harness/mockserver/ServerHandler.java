package com.thistech.vex.harness.mockserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author Dongsong
 */
public abstract class ServerHandler<T extends AConfiguration> extends SimpleChannelInboundHandler<FullHttpRequest> {
    protected final static Logger log = LoggerFactory.getLogger(ServerHandler.class);

    protected abstract HandlerConfiguration configuration();

    protected abstract String handle(ChannelHandlerContext ctx, FullHttpRequest msg);

    @PostConstruct
    protected void init() {
        log.info("Init " + this.configuration().getPath() + " Handler with configuration: \r\n" + this.configuration());
    }

    protected void beforeWriteResponse(ChannelHandlerContext ctx, FullHttpRequest msg, FullHttpResponse response) {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        FullHttpResponse response = this.generateResponse(this.handle(ctx, msg));
        this.beforeWriteResponse(ctx, msg, response);
        ctx.writeAndFlush(response);
    }

    protected FullHttpResponse generateResponse(String responseContent) {
        FullHttpResponse response;
        if (StringUtils.isEmpty(responseContent)) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK);
        } else {
            response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, OK, Unpooled.copiedBuffer(
                    responseContent, CharsetUtil.UTF_8));
        }
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        return response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //ctx.flush();
    }
}
