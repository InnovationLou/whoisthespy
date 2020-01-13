package com.lck.whoisthespy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private int id;

    /**
     * 房间名
     */
    @Column(name = "room_name")
    private String roomName;

    /**
     * 房主id
     */
    @Column(name = "host_id",nullable = false)
    private Integer hostId;

    /**
     * 房间人数上限
     */
    @Column(name = "max_player",nullable = false)
    private Integer maxPlayer;
}
