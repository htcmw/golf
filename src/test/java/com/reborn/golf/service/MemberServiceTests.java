package com.reborn.golf.service;

import com.reborn.golf.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberServiceTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberService memberService;

    @Test
    public void registerTest(){
        IntStream.rangeClosed(1,10).forEach( i -> {
            MemberDto member = MemberDto.builder()
                    .email("user" + i +"@naver.com")
                    .password("1111")
                    .name("유저"+ i)
                    .phone("010" + (int)(Math.random() * (int)Math.pow(10,8)))
                    .address("서울" + i)
                    .build();
            System.out.println(member);
            memberService.register(member);
        });

    }

    @Test
    public void readTest(){
        System.out.println(memberService.read("user1@naver.com"));
    }

    @Test
    public void modifyTest(){
        MemberDto memberDto = MemberDto.builder()
                .email("user9@naver.com")
                .password("1111")
                .name("유저9")
                .phone("010" + (int)(Math.random() * (int)Math.pow(10,8)))
                .address("서울" + 9)
                .build();
        memberService.modify(memberDto);
    }
    @Test
    public void removeTest(){
        memberService.remove("user10@naver.com");
    }
}
