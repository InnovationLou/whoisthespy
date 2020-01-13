package com.lck.whoisthespy.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Test")
public class TestController {

    @ApiOperation("你好")
    @GetMapping("/hello")
    public String hello(){
        return "Hello!";
    }
}
