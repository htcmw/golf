package com.reborn.golf.controller;

import com.reborn.golf.dto.customerservice.NoticeDto;
import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
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


/*
* 공지사항 컨트롤러
* */
@Log4j2
@RestController
@RequestMapping("/notices")
@Validated
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    //모든 공지사항 목록을 출력
    @GetMapping
    public ResponseEntity<PageResultDto<Object[], NoticeDto>> getList(PageRequestDto pageRequestDto) {
        PageResultDto<Object[], NoticeDto> noticeDtoList = noticeService.getList(pageRequestDto);
        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    //공지사항 조회
    @GetMapping("/{idx}")
    public ResponseEntity<NoticeDto> read(@PathVariable @Min(1) Long idx) {
        NoticeDto noticeDto = noticeService.read(idx);
        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    //공지사항 등록
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid NoticeDto noticeDto) {
        Long num = noticeService.register(authMemeberDto.getIdx(),noticeDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //공지사항 수정
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<String> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid NoticeDto noticeDto) {
        noticeService.modify(authMemeberDto.getIdx(), noticeDto);
        return new ResponseEntity<>("Modification was successful", HttpStatus.OK);
    }

    //공지사항 삭제
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable @Min(1) Long idx) {
        noticeService.remove(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>("Deletion was successful", HttpStatus.OK);
    }
}
