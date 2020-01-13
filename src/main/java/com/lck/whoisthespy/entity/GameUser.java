package com.lck.whoisthespy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "game_user")
public class GameUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * 游戏id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 游戏昵称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 头像
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 积分
     */
    @Column(name = "score")
    private Integer score;

    /**
     * 处于哪个房间，若为null，未在游戏中
     */
    @Column(name = "in_room")
    private Integer inRoom;

    /**
     * 是否准备
     */
    @Column(name = "is_ready")
    private Boolean isReady;

    /**
     * 是否生存
     */
    @Column(name = "is_alive")
    private Boolean isAlive;
}
