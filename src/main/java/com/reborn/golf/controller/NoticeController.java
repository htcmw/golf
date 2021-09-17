package com.reborn.golf.controller;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Log4j2
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
@Validated
public class NoticeController {

    private final NoticeService noticeService;

    //모든 공지사항 목록을 출력
    @GetMapping("/list")
    public ResponseEntity<PageResultDto<Notice, NoticeDto>> getList(PageRequestDto pageRequestDto) {
        PageResultDto<Notice, NoticeDto> noticeDtoList = noticeService.getList(pageRequestDto);
        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    //공지사항 조회
    @GetMapping("/read/{num}")
    public ResponseEntity<NoticeDto> read(@PathVariable @Min(1) Long num) {
        NoticeDto noticeDto = noticeService.read(num);
        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    //공지사항 등록
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid NoticeDto noticeDto) {
        String email = authMemeberDto.getUsername();

        Long num = noticeService.register(email, noticeDto);

        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //공지사항 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modify")
    public ResponseEntity<String> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid NoticeDto noticeDto) {
        String email = authMemeberDto.getUsername();

        noticeService.modify(email, noticeDto);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    //공지사항 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{num}")
    public ResponseEntity<String> remove(@PathVariable @Min(1) Long num) {

        noticeService.remove(num);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
