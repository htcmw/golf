package com.reborn.golf.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/api/")
public class MyPageController {

    @GetMapping(value = "/list",  produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> read(){
        return new ResponseEntity<>("list", HttpStatus.OK);
    }

}
