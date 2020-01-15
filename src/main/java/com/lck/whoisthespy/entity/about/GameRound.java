package com.lck.whoisthespy.entity.about;

import com.lck.whoisthespy.entity.GameUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class GameRound {


    private String roundKey;
    /**
     * 哪个房间
     */
    private String roomKey;

    /**
     * 第几对局
     */
    private Integer round;

    /**
     * 发言队列
     */
    private Queue<GameUser> speakQueue;

    /**
     * 当前发言玩家编号
     */
    private String currentSpeakPlayer;

    /**
     * 存活玩家列表ArrayList
     */
    private List<GameUser> alivePlayer;

    /**
     * 投票信息
     * "1|2"    1号投2号
     */
    private Set<String> voteInfo;

    /**
     * 对局结束时淘汰的玩家
     */
    private Integer deadPlyaer;

    /**
     * 计票:
     * 几号，被投几票
     */
    private Map<Integer,Integer> votedNumMap;

    /**
     * 玩家词汇表
     */
    private  Map<GameUser,String> playerWordMap;
}
