package com.lck.whoisthespy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;

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
