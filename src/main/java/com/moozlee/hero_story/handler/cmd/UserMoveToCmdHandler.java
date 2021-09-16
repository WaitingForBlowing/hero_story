package com.moozlee.hero_story.handler.cmd;

import com.moozlee.hero_story.handler.Broadcaster;
import com.moozlee.hero_story.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd>{
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd cmd) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }

        float x = cmd.getMoveToPosX();
        float y = cmd.getMoveToPosY();

        GameMsgProtocol.UserMoveToResult result = GameMsgProtocol.UserMoveToResult
            .newBuilder()
            .setMoveUserId(userId)
            .setMoveToPosX(x)
            .setMoveToPosY(y)
            .build();
        Broadcaster.broadcast(result);
    }
}
