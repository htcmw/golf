package com.reborn.golf.controller;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.NoticeFractionation;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.NoticeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.Min;


/*
* 공지사항, Q&A 컨트롤러
* */
@Log4j2
@RestController
@RequestMapping("/qna")
@Validated
public class QnaController {

    private final NoticeService noticeService;
    private final NoticeFractionation QNAFractionation;

    public QnaController(NoticeService noticeService) {
        this.noticeService = noticeService;
        this.QNAFractionation = NoticeFractionation.QNA;
    }

    //모든 질문 목록을 출력
    @GetMapping
    public ResponseEntity<PageResultDto<Object[], NoticeDto>> getList(PageRequestDto pageRequestDto) {
        PageResultDto<Object[], NoticeDto> noticeDtoList = noticeService.getList(pageRequestDto, QNAFractionation);
        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    //조회
    @GetMapping("/{num}")
    public ResponseEntity<NoticeDto> read(@PathVariable @Min(1) Long num) {
        NoticeDto noticeDto = noticeService.read(num, QNAFractionation);
        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    //등록
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestParam @Nullable  Long qnaIdx, @RequestBody @Valid NoticeDto noticeDto) {
        Long num = noticeService.register(authMemeberDto.getIdx(), qnaIdx, noticeDto, QNAFractionation);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //수정
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<String> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid NoticeDto noticeDto) {
        noticeService.modify(authMemeberDto.getIdx(), noticeDto, QNAFractionation);
        return new ResponseEntity<>("Modification was successful", HttpStatus.OK);
    }

    //삭제
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @DeleteMapping("/{num}")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable @Min(1) Long num) {
        noticeService.remove(authMemeberDto.getIdx(), num, QNAFractionation);
        return new ResponseEntity<>("Deletion was successful", HttpStatus.OK);
    }

}
