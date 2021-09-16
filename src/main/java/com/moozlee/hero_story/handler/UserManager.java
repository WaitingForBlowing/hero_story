package com.moozlee.hero_story.handler;

import com.moozlee.hero_story.entity.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class UserManager {
    /**
     * 用户字典，记录在线用户
     */
    private static final Map<Integer, User> _userMap = new HashMap<>();

    private UserManager() {}

    /**
     * 添加在线用户
     * @param user
     */
    public static void addUser(User user) {
        if (null == user || null == user.getUserId() || null == user.getHeroAvatar()) {
            return;
        }

        _userMap.put(user.getUserId(), user);
    }

    /**
     * 移除在线用户
     * @param id
     */
    public static void removeUserById(Integer id) {
        if (null == id) {
            return;
        }

        _userMap.remove(id);
    }

    /**
     * 获取在线用户列表
     * @return
     */
    public static Collection<User> listUser() {
        return _userMap.values();
    }
}
