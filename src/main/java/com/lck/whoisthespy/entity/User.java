package com.lck.whoisthespy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wx_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;

    /**
     * 微信的唯一标识id
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 用户昵称
     */
    @Column(name = "nick_name", nullable = false)
    private String nickName;
}
