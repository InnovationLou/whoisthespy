package com.lck.whoisthespy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "host_id")
    private Integer hostId;

    @Column(name = "player_num")
    private Integer playerNum;
}
