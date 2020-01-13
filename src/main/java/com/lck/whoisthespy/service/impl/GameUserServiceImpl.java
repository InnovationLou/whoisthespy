package com.lck.whoisthespy.service.impl;

import com.lck.whoisthespy.repository.GameUserRepository;
import com.lck.whoisthespy.service.GameUserService;
import com.lck.whoisthespy.util.ControllerUtil;
import com.lck.whoisthespy.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameUserServiceImpl implements GameUserService {

    @Autowired
    private GameUserRepository resp;

    @Override
    public ResponseVO getAllGameUser() {
        return ControllerUtil.getDataResult(resp.findAll());
    }
}
