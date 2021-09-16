package com.moozlee.hero_story.handler.pipline;

import com.google.protobuf.GeneratedMessageV3;
import com.moozlee.hero_story.handler.Broadcaster;
import com.moozlee.hero_story.handler.UserManager;
import com.moozlee.hero_story.handler.cmd.*;
import com.moozlee.hero_story.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * 消息处理器
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        Broadcaster.removeChannel(ctx.channel());
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }

        UserManager.removeUserById(userId);
        GameMsgProtocol.UserQuitResult result = GameMsgProtocol.UserQuitResult
            .newBuilder()
            .setQuitUserId(userId)
            .build();

        Broadcaster.broadcast(result);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        ICmdHandler<? extends GeneratedMessageV3> handler = CmdHandlerFactory.create(msg.getClass());

        if ( null != handler) {
            handler.handle(ctx, cast(msg));
        }
    }

    private static <T extends GeneratedMessageV3> T cast(Object msg) {
        return null == msg ? null : (T)msg;
    }
}
