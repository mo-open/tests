package com.thistech.vex.harness.mockserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @author Dongsong
 */
@SpringBootApplication
@EnableConfigurationProperties
public class MockServers implements CommandLineRunner {
    private final static Logger log = LoggerFactory.getLogger(MockServers.class);
    @Autowired
    private CommonConfig serverSetting;

    @Autowired
    private List<ServerHandler> handlers;

    @Override
    public void run(String... strings) throws Exception {
        log.info("Server configuration: " + this.serverSetting);

        if (this.handlers == null || this.handlers.isEmpty()) {
            throw new IllegalArgumentException("No Handler Configured");
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(serverSetting.getAcceptors());
        EventLoopGroup workerGroup = new NioEventLoopGroup(serverSetting.getWorkers());
        try {
            ChannelFuture channelFuture = null;
            for (final ServerHandler serverHandler : this.handlers) {
                if (!serverHandler.configuration().isEnable()) {
                    continue;
                }
                final SslContext sslCtx;
                if (serverHandler.configuration().isSsl()) {
                    SelfSignedCertificate ssc = new SelfSignedCertificate();
                    sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
                } else {
                    sslCtx = null;
                }
                // Configure the server.
                ServerBootstrap bootstrap = new ServerBootstrap();

                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childOption(ChannelOption.SO_REUSEADDR, true)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                if (sslCtx != null) {
                                    pipeline.addLast(sslCtx.newHandler(ch.alloc()));
                                }
                                pipeline.addLast(new HttpServerCodec());
                                //pipeline.addLast("decoder", new HttpRequestDecoder());
                                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                                //pipeline.addLast("encoder", new HttpResponseEncoder());
                                //pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
                                //pipeline.addLast("deflate", new HttpContentCompressor(1));
                                pipeline.addLast(serverHandler);
                            }
                        });

                Channel ch = bootstrap.bind("0.0.0.0", serverHandler.configuration().getPort()).sync().channel();
                channelFuture = ch.closeFuture();
            }
            if (channelFuture != null) {
                channelFuture.sync();
            }
        } catch (Exception ex) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        log.info("Starting MockServer ...");
        SpringApplication.run(MockServers.class, args);
    }
}
