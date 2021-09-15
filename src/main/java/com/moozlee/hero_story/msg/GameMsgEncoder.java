package com.moozlee.hero_story.msg;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof GeneratedMessageV3)) {
            super.write(ctx, msg, promise);
            return;
        }

        int msgCode = -1;

        if (msg instanceof GameMsgProtocol.UserEntryResult) {
            msgCode = GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereResult) {
            msgCode = GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.UserMoveToResult) {
            msgCode = GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.UserQuitResult) {
            msgCode = GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE;
        }else {
            log.error("无法识别消息类型, msgClazz = " + msg.getClass().getName());
            return;
        }

        byte[] body = ((GeneratedMessageV3) msg).toByteArray();

        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeShort(0);
        byteBuf.writeShort((short)msgCode);
        byteBuf.writeBytes(body);

        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
        super.write(ctx, frame, promise);
    }
}
