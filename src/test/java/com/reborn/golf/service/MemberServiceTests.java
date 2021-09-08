package com.reborn.golf.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberServiceTests {

    @Autowired
    MemberService memberService;
    @Test
    public void insertMemberDummies(){
        IntStream.rangeClosed(1,100).forEach(null);

    }
}
