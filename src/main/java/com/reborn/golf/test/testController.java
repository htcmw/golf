package com.reborn.golf.test;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log4j2
public class testController {
    @GetMapping("/payment/payment")
    public void pay(){

    }

    @PostMapping("/paymen")
    public @ResponseBody
    Test suc(@RequestBody Test test){

        return test;
    }
}
