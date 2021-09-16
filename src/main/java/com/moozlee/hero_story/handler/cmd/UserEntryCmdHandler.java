package com.moozlee.hero_story.handler.cmd;

import com.moozlee.hero_story.entity.User;
import com.moozlee.hero_story.handler.Broadcaster;
import com.moozlee.hero_story.handler.UserManager;
import com.moozlee.hero_story.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {

    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd cmd) {
        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();

        GameMsgProtocol.UserEntryResult result = GameMsgProtocol.UserEntryResult
            .newBuilder()
            .setUserId(userId)
            .setHeroAvatar(heroAvatar)
            .build();

        // 用户放入字典
        User newUser = new User(userId, heroAvatar);
        UserManager.addUser(newUser);

        // 信道同用户id绑定
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
        Broadcaster.broadcast(result);
    }
}
