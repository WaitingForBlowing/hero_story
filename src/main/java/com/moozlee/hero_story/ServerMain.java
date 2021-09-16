package com.moozlee.hero_story;

import com.moozlee.hero_story.handler.cmd.CmdHandlerFactory;
import com.moozlee.hero_story.handler.pipline.GameMsgHandler;
import com.moozlee.hero_story.msg.GameMsgDecoder;
import com.moozlee.hero_story.msg.GameMsgEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        CmdHandlerFactory.init();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                sc.pipeline().addLast(
                    new HttpServerCodec(), // HTTP协议的编解码器
                    new HttpObjectAggregator(65535), // 内容长度限制
                    new WebSocketServerProtocolHandler("/websocket"), // WebSocket协议处理器
                    new GameMsgDecoder(), // 自定义的消息解码器
                    new GameMsgEncoder(), // 自定义消息编码器
                    new GameMsgHandler() // 自定义消息处理器
                );
            }
        });

        try {
            ChannelFuture future = bootstrap.bind(12345).sync();
            if (future.isSuccess()) {
                log.info("服务器启动");
            }

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
