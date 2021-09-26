package com.reborn.golf.controller;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Notice;
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
    public ResponseEntity<PageResultDto<Notice, NoticeDto>> getList(PageRequestDto pageRequestDto) {
        PageResultDto<Notice, NoticeDto> noticeDtoList = noticeService.getList(pageRequestDto, QNAFractionation);
        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    //조회
    @GetMapping("/{num}")
    public ResponseEntity<NoticeDto> readQuestion(@PathVariable @Min(1) Long num) {
        NoticeDto noticeDto = noticeService.read(num, QNAFractionation);
        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    //등록
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Long> registerQuestion(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestParam Long qnaIdx, @RequestBody @Valid NoticeDto noticeDto) {
        Integer idx = authMemeberDto.getIdx();
        Long num = noticeService.register(idx, qnaIdx, noticeDto, QNAFractionation);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //수정
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<String> modifyQuestion(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid NoticeDto noticeDto) {
        Integer idx = authMemeberDto.getIdx();
        noticeService.modify(idx, noticeDto, QNAFractionation);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    //삭제
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{num}")
    public ResponseEntity<String> removeQuestion(@PathVariable @Min(1) Long num) {
        noticeService.remove(num, QNAFractionation);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
