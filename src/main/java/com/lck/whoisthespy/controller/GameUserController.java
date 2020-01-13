package com.lck.whoisthespy.controller;

import com.lck.whoisthespy.service.GameUserService;
import com.lck.whoisthespy.vo.ResponseVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameUser")
public class GameUserController {

    @Autowired
    GameUserService gameUserService;

    @ApiOperation("获取所有游戏玩家")
    @GetMapping("")
    public ResponseVO getAllGameUser(){
        return gameUserService.getAllGameUser();
    }
}
