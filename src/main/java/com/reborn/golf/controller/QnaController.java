package com.reborn.golf.controller;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.QnaDto;
import com.reborn.golf.entity.NoticeFractionation;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.NoticeService;
import com.reborn.golf.service.QnaService;
import lombok.RequiredArgsConstructor;
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


@Log4j2
@RestController
@RequestMapping("/qnas")
@Validated
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    //모든 질문 목록을 출력
    @GetMapping
    public ResponseEntity<PageResultDto<Object[], QnaDto>> getList(PageRequestDto pageRequestDto) {
        PageResultDto<Object[], QnaDto> qnaDtoList = qnaService.getList(pageRequestDto);
        return new ResponseEntity<>(qnaDtoList, HttpStatus.OK);
    }

    //조회
    @GetMapping("/{idx}")
    public ResponseEntity<QnaDto> read(@PathVariable @Min(1) Long idx) {
        QnaDto qnaDto = qnaService.read(idx);
        return new ResponseEntity<>(qnaDto, HttpStatus.OK);
    }

    //등록
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @PostMapping("/{idx}")
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable @Min(0) Long idx, @RequestBody @Valid QnaDto qnaDto) {
        Long num = qnaService.register(authMemeberDto.getIdx(), idx, qnaDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //수정
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<String> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid QnaDto qnaDto) {
        qnaService.modify(authMemeberDto.getIdx(), qnaDto);
        return new ResponseEntity<>("Modification was successful", HttpStatus.OK);
    }

    //삭제
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable @Min(1) Long idx) {
        qnaService.remove(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>("Deletion was successful", HttpStatus.OK);
    }

}
