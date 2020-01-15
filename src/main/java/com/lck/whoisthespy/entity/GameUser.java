package com.lck.whoisthespy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "game_user")
public class GameUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * 游戏编号
     */
    private String gameNo;

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
     * roomKey
     * 处于哪个房间，若为null，未在游戏中
     */
    @Column(name = "room_key")
    private String roomKey;

    /**
     * 是否卧底
     */
    @Column(name = "is_Spy")
    private Boolean isSpy;
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


    //创建房间构造
    public GameUser(Integer userId, String userName, String gameNo, String roomKey, Boolean isReady) {
        this.userId=userId;
        this.userName=userName;
        this.gameNo=gameNo;
        this.roomKey=roomKey;
        this.isReady=isReady;
    }
}
