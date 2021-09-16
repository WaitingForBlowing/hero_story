package com.moozlee.hero_story.handler.cmd;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * 处理指令的处理器顶层接口
 * @param <T>
 */
public interface ICmdHandler<T extends GeneratedMessageV3> {
    void handle(ChannelHandlerContext ctx, T cmd);
}
