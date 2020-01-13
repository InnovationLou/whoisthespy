package com.lck.whoisthespy.service.impl;

import com.lck.whoisthespy.entity.Room;
import com.lck.whoisthespy.repository.GameUserRepository;
import com.lck.whoisthespy.repository.RoomRepository;
import com.lck.whoisthespy.service.GameUserService;
import com.lck.whoisthespy.util.ControllerUtil;
import com.lck.whoisthespy.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameUserServiceImpl implements GameUserService {

    @Autowired
    private GameUserRepository resp;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public ResponseVO getAllGameUser() {
        return ControllerUtil.getDataResult(resp.findAll());
    }

    @Override
    public Room createRoom(Integer userId,Integer maxPlayer) {
        String userName=resp.findGameUserByUserId(userId).getUserName();
        Room room=new Room(new Integer(1),"欢迎加入"+userName+"的房间",userId,maxPlayer);
        roomRepository.save(room);
        return room;
    }

    @Override
    public String getUserNameById(Integer userId) {
        String userName=resp.findGameUserByUserId(userId).getUserName();
        return userName;
    }
}
