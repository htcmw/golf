package com.reborn.golf.controller;

import com.reborn.golf.entity.Member;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // 회원가입
    @PostMapping("/join")
    public void join(@RequestBody Member member) {
    }
}
