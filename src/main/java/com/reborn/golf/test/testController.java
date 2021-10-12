package com.reborn.golf.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class testController {
    @GetMapping("/payment/payment")
    public void pay(){

    }

    @PostMapping("/payment/success")
    public void suc(@RequestBody Test test){
        System.out.println(test);

    }
}
