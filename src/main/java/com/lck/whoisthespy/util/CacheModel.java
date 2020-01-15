package com.lck.whoisthespy.util;

import com.lck.whoisthespy.entity.Room;
import com.lck.whoisthespy.entity.about.GameRound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CacheModel {

    //声明一个map,用来作为缓存模型
    private static Map<String, Room> roomMap = new HashMap<String, Room>();

    private static Map<String, GameRound> roundMap=new HashMap<String, GameRound>();

    public synchronized static Room getRoom(String key) {
        Room value = roomMap.get(key);
        return value;
    }
    public synchronized static Boolean putRoom(String key,Room room){
        if(roomMap.containsKey(key)){
            return false;
        }
        roomMap.put(key,room);
        return true;
    }
    public synchronized static Boolean saveRoom(String key,Room room){
        if(!roomMap.containsKey(key)){
            return false;
        }
        roomMap.put(key, room);
        return true;
    }
    public synchronized static Set<Map.Entry<String, Room>> entrySet(){
        return roomMap.entrySet();
    }

//    public synchronized static GameRound getGameRound(String roundKey){
//        GameRound round=roundMap.get(roundKey);
//        return round;
//    }

//    public synchronized static void putRound(String roundKey,GameRound gameRound){
//        roundMap.put(roundKey,gameRound);
//    }
    public synchronized static GameRound getGameRoundByRoomKey(String roomKey){

        Room room=roomMap.get(roomKey);
        GameRound round=room.getGameRound();
        return round;
    }
    public synchronized static void saveGameRound(GameRound round,String roomKey){
        Room room=roomMap.get(roomKey);
        room.setGameRound(round);
    }

}
