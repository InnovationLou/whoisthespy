package com.lck.whoisthespy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lck.whoisthespy.entity.CommuMsg;
import com.lck.whoisthespy.service.GameUserService;
import com.lck.whoisthespy.util.CacheModel;
import com.lck.whoisthespy.util.ControllerUtil;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
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

    @Autowired
    GameUserService gameUserService;

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
        String msg=obj.getString("msg");
        sendMessage(ControllerUtil.getDataResult(new CommuMsg("test","啦啦啦连接成功")).toString());
        //vote(head, msg);

        switch (head){
            case "IMhost":
                Integer userId=Integer.parseInt(JSON.parseObject(msg).getString("userId"));
                Integer maxPlayer=Integer.parseInt(JSON.parseObject(msg).getString("maxPlayer"));
                gameUserService.createRoom(userId,maxPlayer);

                break;
            case "IMplayer":
                break;
            case "getReady":
                break;
            default:
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
