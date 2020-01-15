package com.lck.whoisthespy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lck.whoisthespy.entity.CommuMsg;
import com.lck.whoisthespy.entity.GameUser;
import com.lck.whoisthespy.entity.Room;
import com.lck.whoisthespy.entity.about.GameRound;
import com.lck.whoisthespy.repository.GameUserRepository;
import com.lck.whoisthespy.service.GameUserService;
import com.lck.whoisthespy.util.CacheModel;
import com.lck.whoisthespy.util.ControllerUtil;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Cache;

import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/game/{userId}")
@Component
public class WebSocketServer {

    static Logger logger= LoggerFactory.getLogger(WebSocketServer.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<Integer,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private Integer userId=null;

    //自定义缓存模型<String,Object>
    private static CacheModel cacheModel;

    /**
     * 直接注入将出现空指针异常
     * Autowired是单例模式
     * 而WebSocket是多实例模式。
     * 使用静态注入
     */
    @Autowired
    private static GameUserService gameUserService;

    @Autowired
    public void setChatService(GameUserService gameUserService) {
        WebSocketServer.gameUserService=gameUserService;
    }

    /**
     * ****************************************************************************************
     */

//    private Set<VoteInfo> voteSet=new HashSet<>();
//    private HashMap<Integer,Integer> voteStat=new HashMap<>();
//    //假设房间有6人
//    private Integer playerNum=6;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") Integer userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }
        logger.info("用户登录:"+userId+"当前在线人数为:"+getOnlineCount());
        //sendMessage("hahahahaha...");
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {

        JSONObject obj=JSON.parseObject(message);
        String head=obj.getString("head");
        JSONObject msg=obj.getJSONObject("msg");
        //sendMessage(ControllerUtil.getDataResult(new CommuMsg("test","啦啦啦连接成功")).toString());
        //vote(head, msg);

        if(head.equals("IMhost")){
            Integer userId=Integer.parseInt(msg.getString("userId"));
            Integer maxPlayer=Integer.parseInt(msg.getString("maxPlayer"));
            String userName=msg.getString("userName");

            //new 房间
            Room room=new Room("room"+userId,"欢迎加入"+userName+"的房间",userId,maxPlayer,new ArrayList<>(),0,false,new GameRound());
            room.getPlayers().add(new GameUser(userId,userName,"1","room"+userId,true));
            room.setPlayerNum(room.getPlayerNum()+1);
            CacheModel.putRoom("room"+userId,room);
            logger.info(room+"创建成功");
            CommuMsg commuMsg=new CommuMsg("createSuccess", room);
            sendMessage(JSON.toJSONString(commuMsg));
        }
        if(head.equals("IMplayer")){
            Integer userId=Integer.parseInt(msg.getString("userId"));
            //Integer roomId=Integer.parseInt(msg.getString("roomId"));
            String userName=msg.getString("userName");
            Integer hostId=Integer.parseInt(msg.getString("hostId"));
            Room room= (Room) CacheModel.getRoom("room"+hostId);
            String playerNo= String.valueOf(room.getPlayerNum()+1);
            GameUser player=new GameUser(userId,userName,playerNo,"room"+hostId,false);
            room.getPlayers().add(player);
            CacheModel.saveRoom("room"+hostId,room);
            logger.info("加入房间:room"+hostId+"成功");
            sendMessage(JSON.toJSONString(new CommuMsg("joinSuccess",player)));

            sendInRoomPlayersMessage(CacheModel.getRoom("room"+hostId),CacheModel.getRoom("room"+hostId).getPlayers());

        }

        if(head.equals("getReady")){
            String roomKey=msg.getString("roomKey");
            Integer userId=Integer.parseInt(msg.getString("userId"));
            String playerNo=msg.getString("playerNo");
            //准备就绪
            Room room=CacheModel.getRoom(roomKey);
            GameUser player=room.getPlayers().get(Integer.parseInt(playerNo));
            player.setIsReady(true);
            CacheModel.saveRoom(roomKey,room);
            sendMessage(JSON.toJSONString(new CommuMsg("ReadyOK", JSON.toJSONString(player))));
            sendInRoomPlayersMessage(CacheModel.getRoom(roomKey),CacheModel.getRoom(roomKey));
        }

        if(head.equals("startGame")){
            String roomKey=msg.getString("roomKey");
            //Integer hostId=Integer.parseInt(msg.getString("hostId"));
            Room room=CacheModel.getRoom(roomKey);
            room.setGameStatus(true);
            //初始化房间对局信息
            GameRound round=room.getGameRound();
            round.setAlivePlayer(room.getPlayers());
            round.setRoomKey(roomKey);
            round.setRound(1);
            //发言队列
            round.setSpeakQueue(new LinkedList<>(round.getAlivePlayer()));
            //设置第一个人发言
            round.setCurrentSpeakPlayer(round.getSpeakQueue().element().getGameNo());
            round.setVoteInfo(new HashSet<>());
            Map<Integer,Integer> votedNumMap=new HashMap<>();
            Map<GameUser,String> playerWordMap=new HashMap<>();
            for (GameUser player: round.getAlivePlayer()){
                votedNumMap.put(Integer.parseInt(player.getGameNo()),0);
                //这里从数据库获取词汇
                playerWordMap.put(player,"词汇1");
                player.setIsSpy(false);
                if(player.getGameNo().equals(2)){
                    playerWordMap.put(player,"卧底词");
                    player.setIsSpy(true);
                }
            }
            round.setVotedNumMap(votedNumMap);

            round.setPlayerWordMap(playerWordMap);
            CacheModel.saveRoom(roomKey,room);
            //sendMessage(ControllerUtil.getDataResult(new CommuMsg("GameStartedOK", JSON.toJSONString("游戏开始"))).toString());
            sendMessage("即将开始游戏");
            //暂时全部信息发送，可以根据房间内的玩家单独发送
            //各玩家均可以拿到词汇和发言顺序
            sendInRoomPlayersMessage(CacheModel.getRoom(roomKey),CacheModel.getRoom(roomKey).getGameRound());
        }

        if(head.equals("speak")){
            String roomKey=msg.getString("roomKey");
            String gameNo=msg.getString("gameNo");
            String content=msg.getString("content");
            Room room=CacheModel.getRoom(roomKey);
            GameRound round=room.getGameRound();
            sendInRoomPlayersMessage(room,new CommuMsg("playerSpeak",gameNo+"说："+content));
            round.getSpeakQueue().poll();
            round.setCurrentSpeakPlayer(round.getSpeakQueue().element().getGameNo());
            //round.setSpeakQueue();
            //发给前端下一轮发言的玩家号
            sendInRoomPlayersMessage(room,new CommuMsg("playerSpeakOK",round.getCurrentSpeakPlayer()));
        }

        if(head.equals("vote")){
            String roomKey=msg.getString("roomKey");
            //String gameNo=msg.getString("gameNo");
            String voteMsg=msg.getString("voteMsg");
            Room room=CacheModel.getRoom(roomKey);
            GameRound round=CacheModel.getGameRoundByRoomKey(roomKey);
            Set<String> voteInfo=round.getVoteInfo();
            voteInfo.add(voteMsg);
            sendMessage("received"+voteMsg);

            //收到所有玩家投票,解析set，计算结果
            if(voteInfo.size()==round.getAlivePlayer().size()){
                Map<Integer, Integer> map = round.getVotedNumMap();
                for(String s:voteInfo){
                    Integer my=Integer.parseInt(voteMsg.split("\\|")[0]);
                    Integer target=Integer.parseInt(voteMsg.split("\\|")[1]);
                    map.put(target,map.get(target)+1);
                }
                Integer maxVotedPlayer = null;
                Integer maxVotedNum=0;
                //求出最大投票数
                for (Map.Entry<Integer,Integer> entry:map.entrySet()) {
                    if (entry.getValue()>maxVotedNum){
                        maxVotedNum=entry.getValue();
                        maxVotedPlayer=entry.getKey();
                    }
                }
                round.setDeadPlyaer(maxVotedPlayer);
                List<GameUser> players=  round.getAlivePlayer();
                for(GameUser player: players){
                    if(Integer.valueOf(player.getGameNo())==maxVotedPlayer){
                        players.remove(player);
                        //玩家死亡
                        player.setIsAlive(false);
                    }
                }
                Queue<GameUser> queue=round.getSpeakQueue();
                for(GameUser player:queue){
                    if(player.getGameNo().equals(String.valueOf(maxVotedPlayer))){
                        queue.remove(player);
                    }
                }

                //此处还需要判断游戏是否结束，场上存活人数，被淘汰的玩家是否为卧底
                //返回投票结果,前端可以准备下一轮的speak
                sendInRoomPlayersMessage(room,new CommuMsg("voteResult",maxVotedPlayer+":"+maxVotedNum));
                sendInRoomPlayersMessage(room,new CommuMsg("nextRoundSpeakQueue",round.getSpeakQueue()));
            }
        }
    }


    /**
     * 发送给处于room中所有玩家 obj的信息
     * @param room
     * @param obj
     */
    private void sendInRoomPlayersMessage(Room room,Object obj) {

        List<GameUser> players=room.getPlayers();
        List<Integer> userList=new ArrayList<>();

        for(GameUser p:players){
            userList.add(p.getUserId());
        }

            for(Map.Entry<Integer, WebSocketServer> entry:
                    webSocketMap.entrySet()) {
                for(Integer i:userList){
                    if(entry.getKey().equals(i)){
                        //sendMessage();
                        entry.getValue().session.getAsyncRemote().sendText(JSON.toJSONString(new CommuMsg("roomBroadcast", obj)));
                    }
                }
            }

    }
//    private void vote(String head, String msg) {
//        if(head.equals("voteInfo")){
//            //投票信息,head="voteInfo"
//            VoteInfo voteInfo= JSON.parseObject(msg,VoteInfo.class);
//
//            voteSet.add(voteInfo);//true
//
//            //int[] stat=new int[playerNum];//int default :0
//            while (voteSet.size()!=playerNum){
//                Integer maxVotedKey=null;
//                Integer maxVotedNum=0;
//                for (VoteInfo v:
//                     voteSet) {
//                    voteStat.put(v.getTarget(),voteStat.get(v.getTarget())+1);
//                }
//                for (VoteInfo v:
//                        voteSet) {
//                    Integer tmp=voteStat.get(v.getTarget());
//                    if(tmp>maxVotedNum){
//                        maxVotedNum=tmp;
//                        maxVotedKey=v.getTarget();
//                    }
//                }
//                sendMessage(maxVotedKey+"被投了:"+maxVotedNum);
//
//            }
//        }
//    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        logger.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }
    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }


    /**
     * 主动推送
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
