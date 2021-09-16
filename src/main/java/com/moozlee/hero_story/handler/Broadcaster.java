package com.moozlee.hero_story.handler;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public final class Broadcaster {
    /**
     * 客户端信道组
     */
    private static final ChannelGroup _group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster() {

    }
    /**
     * 添加信道
     * @param channel
     */
    public static void addChannel(Channel channel) {
        if (null == channel) {
            return;
        }
        _group.add(channel);
    }

    /**
     * 移除信道
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        if (null == channel) {
            return;
        }
        _group.remove(channel);
    }

    /**
     * 广播消息
     * @param msg
     */
    public static void broadcast(Object msg) {
        if (null == msg) {
            return;
        }

        _group.writeAndFlush(msg);
    }
}
