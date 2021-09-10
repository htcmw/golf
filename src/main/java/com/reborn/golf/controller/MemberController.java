package com.reborn.golf.controller;

import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/register")
    public void register(@RequestBody MemberDto memberDto) {
        memberService.register(memberDto);
    }

    @PostMapping(value = "/read.role")
    public ResponseEntity<MemberDto> read(@RequestBody Map<String,String> param) {
        MemberDto memberDto = memberService.read(param.get("email"));
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    @PutMapping("/modify.role")
    public void modify(@RequestBody MemberDto memberDto){
        memberService.modify(memberDto);
    }

    @DeleteMapping("/remove.role")
    public void remove(@RequestBody Map<String,String> param){
        memberService.remove(param.get("email"));
    }


}
