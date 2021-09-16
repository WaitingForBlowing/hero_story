package com.moozlee.hero_story.handler.cmd;

import com.google.protobuf.GeneratedMessageV3;
import com.moozlee.hero_story.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令处理器的工厂
 */
public final class CmdHandlerFactory {

    private static Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handleMap = new HashMap<>();

    private CmdHandlerFactory() {}

    public static void init() {
        _handleMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        _handleMap.put(GameMsgProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
        _handleMap.put(GameMsgProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
    }

    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> clazz) {
        return null == clazz ? null : _handleMap.get(clazz);
    }
}
