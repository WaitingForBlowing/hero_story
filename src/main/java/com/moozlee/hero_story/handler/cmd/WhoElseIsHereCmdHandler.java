package com.moozlee.hero_story.handler.cmd;

import com.moozlee.hero_story.entity.User;
import com.moozlee.hero_story.handler.UserManager;
import com.moozlee.hero_story.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;

public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd cmd) {
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        for (User u : UserManager.listUser()) {
            if (null == u) {
                continue;
            }

            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo
                .newBuilder()
                .setUserId(u.getUserId())
                .setHeroAvatar(u.getHeroAvatar());

            resultBuilder.addUserInfo(userInfoBuilder);
        }

        GameMsgProtocol.WhoElseIsHereResult result = resultBuilder.build();
        ctx.writeAndFlush(result);
    }
}
