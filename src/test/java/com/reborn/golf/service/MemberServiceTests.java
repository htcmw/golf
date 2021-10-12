package com.reborn.golf.service;

import com.reborn.golf.dto.user.MemberDto;
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
    public void registerTestUser() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            MemberDto member = MemberDto.builder()
                    .email("user" + i + "@naver.com")
                    .password("1234")
                    .phone("010" + (int) (Math.random() * (int) Math.pow(10, 8)))
                    .name("유저" + i)
                    .address("서울" + i)
                    .fromSocial(false)
                    .build();
            System.out.println(member);
            memberService.register(member);
        });

    }

    @Test
    public void readTest() {
        System.out.println(memberService.read(10));
    }

    @Test
    public void modifyTest() {
        Integer idx = 10;
        MemberDto memberDto = MemberDto.builder()
                .idx(idx)
                .email("user101@naver.com")
                .password("1111")
                .name("유저101")
                .phone("010" + (int) (Math.random() * (int) Math.pow(10, 8)))
                .address("서울" + 101)
                .build();
        memberService.modify(idx, memberDto);
    }

    @Test
    public void removeTest() {
        memberService.remove(10);
    }
}
