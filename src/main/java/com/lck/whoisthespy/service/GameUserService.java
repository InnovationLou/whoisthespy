package com.lck.whoisthespy.service;

import com.lck.whoisthespy.entity.Room;
import com.lck.whoisthespy.vo.ResponseVO;

public interface GameUserService {
    ResponseVO getAllGameUser();

    String getUserNameById(Integer userId);
}
