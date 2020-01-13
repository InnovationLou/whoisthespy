package com.lck.whoisthespy.service;

import com.lck.whoisthespy.entity.Room;
import com.lck.whoisthespy.vo.ResponseVO;

public interface GameUserService {
    ResponseVO getAllGameUser();

    Room createRoom(Integer userId, Integer maxPlayer);

    String getUserNameById(Integer userId);
}
