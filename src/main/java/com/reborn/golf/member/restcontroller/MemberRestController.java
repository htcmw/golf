package com.reborn.golf.member.restcontroller;

import com.reborn.golf.member.dto.MemberDto;
import com.reborn.golf.member.dto.UserType;
import com.reborn.golf.member.service.MemberService;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberRestController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/members")
    public ResponseEntity<String> registerMember(@RequestBody @Valid MemberDto memberDto) {
        memberService.register(memberDto, UserType.USER);
        return new ResponseEntity<>("회원가입을 축하드립니다", HttpStatus.OK);
    }

    //직원 입사
    @PostMapping("/employees")
    public ResponseEntity<String> registerEmployees(@RequestBody @Valid MemberDto memberDto) {
        memberService.register(memberDto, UserType.WORKER);
        return new ResponseEntity<>("회원가입을 축하드립니다", HttpStatus.OK);
    }

    //회원 정보 조회
    @GetMapping(value = {"/members", "/employees"})
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    public ResponseEntity<MemberDto> read(@AuthenticationPrincipal AuthMemeberDto authMemeberDto) {
        MemberDto memberDto = memberService.read(authMemeberDto.getIdx());
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    //회원 정보 수정
    @PutMapping(value = {"/members", "/employees"})
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    public ResponseEntity<String> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid MemberDto memberDto) {
        memberService.modify(authMemeberDto.getIdx(), memberDto);
        return new ResponseEntity<>("회원정보가 변경되었습니다", HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping(value = {"/members", "/employees"})
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto) {
        memberService.remove(authMemeberDto.getIdx());
        return new ResponseEntity<>("회원정보가 삭제되었습니다", HttpStatus.OK);
    }

    // 이메일 찾기
    @PostMapping("/email/search")
    public ResponseEntity<String> searchEmail(@RequestBody @Valid MemberDto memberDto) {
        return new ResponseEntity<>(memberService.searchEmail(memberDto), HttpStatus.OK);
    }

    //비밀번호 찾기
    @PutMapping("/password/search")
    public ResponseEntity<Integer> searchPassword(@RequestBody @Valid MemberDto memberDto) {
        return new ResponseEntity<>(memberService.searchPassword(memberDto), HttpStatus.OK);
    }
}
