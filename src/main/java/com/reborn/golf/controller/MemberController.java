package com.reborn.golf.controller;

import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/create")
    public void create(@RequestBody MemberDto memberDto) {
        memberService.register(memberDto);
    }

    // 회원 정보 조회
    @GetMapping("/read")
    public ResponseEntity<MemberDto> read(String email) {
        MemberDto memberDto = memberService.read(email);
        return new ResponseEntity<MemberDto>(memberDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public void modify(@RequestBody MemberDto memberDto){
        memberService.modify(memberDto);
    }

    @DeleteMapping("/delete")
    public void remove(String email){
        memberService.remove(email);
    }
}
