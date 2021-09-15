package com.moozlee.hero_story.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户形象
     */
    private String heroAvatar;
}
