package com.lck.whoisthespy.service;

import com.lck.whoisthespy.vo.ResponseVO;

public interface GameUserService {
    ResponseVO getAllGameUser();

    ResponseVO createRoom(Integer userId, Integer maxPlayer);
}
