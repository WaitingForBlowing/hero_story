package com.moozlee.hero_story;

import com.moozlee.hero_story.entity.User;
import com.moozlee.hero_story.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理器
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 客户端信道组
     */
    private static final ChannelGroup _group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 用户字典，记录在线用户
     */
    private static final Map<Integer, User> _userMap = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        _group.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        _group.remove(ctx.channel());
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }

        _userMap.remove(userId);
        GameMsgProtocol.UserQuitResult result = GameMsgProtocol.UserQuitResult
            .newBuilder()
            .setQuitUserId(userId)
            .build();

        _group.writeAndFlush(result);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            GameMsgProtocol.UserEntryCmd cmd = (GameMsgProtocol.UserEntryCmd) msg;
            int userId = cmd.getUserId();
            String heroAvatar = cmd.getHeroAvatar();

            GameMsgProtocol.UserEntryResult result = GameMsgProtocol.UserEntryResult
                .newBuilder()
                .setUserId(userId)
                .setHeroAvatar(heroAvatar)
                .build();

            // 用户放入字典
            User newUser = new User(userId, heroAvatar);
            _userMap.put(userId, newUser);

            // 信道同用户id绑定
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

            _group.writeAndFlush(result);
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

            for (User u : _userMap.values()) {
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
        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
            Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
            if (null == userId) {
                return;
            }

            GameMsgProtocol.UserMoveToCmd cmd = (GameMsgProtocol.UserMoveToCmd) msg;
            float x = cmd.getMoveToPosX();
            float y = cmd.getMoveToPosY();

            GameMsgProtocol.UserMoveToResult result = GameMsgProtocol.UserMoveToResult
                .newBuilder()
                .setMoveUserId(userId)
                .setMoveToPosX(x)
                .setMoveToPosY(y)
                .build();
            _group.writeAndFlush(result);
        }
    }
}
