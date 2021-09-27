package com.reborn.golf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/intro")
public class IntroductionController {

    @GetMapping("/company")
    public void introduceCompany(){

    }

    @GetMapping("/team")
    public void introduceTeam(){

    }
    @GetMapping("/token")
    public void introductToken(){

    }

}
