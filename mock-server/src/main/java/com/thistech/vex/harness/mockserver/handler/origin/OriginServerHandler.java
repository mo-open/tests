package com.thistech.vex.harness.mockserver.handler.origin;

import com.google.common.io.Closeables;
import com.google.common.io.LineReader;
import com.thistech.vex.harness.mockserver.AConfiguration;
import com.thistech.vex.harness.mockserver.HandlerConfiguration;
import com.thistech.vex.harness.mockserver.ServerHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by modongsong on 2014/10/13.
 */
@Component
@ConditionalOnProperty(prefix = "servers.origin", name = "enable", havingValue = "true")
@ChannelHandler.Sharable
public class OriginServerHandler extends ServerHandler {
    private StringBuilder responseBuffer = new StringBuilder();

    @Autowired
    private OriginConfiguration originConfiguration;

    @Override
    protected HandlerConfiguration configuration() {
        return this.originConfiguration;
    }

    @PostConstruct
    protected void init() {
        super.init();
        this.genPlayList();
    }

    @Override
    protected String handle(ChannelHandlerContext ctx, FullHttpRequest msg) {
        return this.responseBuffer.toString();
    }

    private void genPlayList() {
        try {
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(
                            this.originConfiguration.getResponseFile());

            Reader reader = new BufferedReader(new InputStreamReader(inputStream));

            LineReader lineReader = new LineReader(reader);
            try {
                String line = null;
                while (true) {
                    line = lineReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if ("".equals(line.trim())) continue;
                    this.responseBuffer.append(line).append("\r\n");
                }
            } finally {
                try {
                    Closeables.close(reader, true);
                } catch (Exception ex) {

                }
            }
        } catch (Exception ex) {
            log.error("Can not file " + this.originConfiguration.getResponseFile(), ex);
        }
    }
}
