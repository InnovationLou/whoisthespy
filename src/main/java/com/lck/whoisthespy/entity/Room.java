package com.lck.whoisthespy.entity;

import com.lck.whoisthespy.entity.about.GameRound;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;


@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class Room {

    /**
     * 房间key
     */
    private String roomKey;

    /**
     * 房间名
     */
    private String roomName;

    /**
     * 房主id
     */
    private Integer hostId;

    /**
     * 房间人数上限
     */
    private Integer maxPlayer;

    /**
     * 玩家列表
     */
    private ArrayList<GameUser> players;

    /**
     * 房间人数
     */
    private Integer playerNum;

    /**
     * 该房间游戏是否开始
     */
    private Boolean gameStatus;

    /**
     * 对局信息
     */
    private GameRound gameRound;
}
