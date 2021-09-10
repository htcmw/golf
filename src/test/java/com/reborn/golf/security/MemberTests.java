package com.reborn.golf.security;

import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.MemberRole;
import com.reborn.golf.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
public class MemberTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMemberExample1(){
        Member member = Member.builder()
                .email("admin@naver.com")

                .build();
        memberRepository.save(member);
    }
    @Test
    public void insertMemberExample2(){
        Member member = Member.builder()
                .email("user@naver.com")
                .password(passwordEncoder.encode("1111"))
                .name("일유저")
                .phone("01009870987")
                .address("서울")
                .fromSocial(false)
                .build();
        memberRepository.save(member);
    }
    @Test
    public void testRead(){
        Optional<Member> result = memberRepository.findByEmail("user@naver.com",false);

        Member member = result.get();

        System.out.println(member);
    }
}
