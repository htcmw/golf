package com.reborn.golf.controller;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.common.ReplyDto;
import com.reborn.golf.entity.Reply;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.ReplyService;
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

@RestController
@RequestMapping("/replies")
@Log4j2
@Validated
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    //공지사항 댓글 목록
    @GetMapping
    public ResponseEntity<PageResultDto<Reply, ReplyDto>> getList(@RequestParam @Min(1) Long noticeNum, PageRequestDto pageRequestDto) {
        PageResultDto<Reply, ReplyDto> resultDto = replyService.getList(noticeNum, pageRequestDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    //댓글 등록
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid ReplyDto replyDto) {
        Integer idx = authMemeberDto.getIdx();
        Long num = replyService.register(idx, replyDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }


    //댓글 수정
    @PreAuthorize("hasRole('USER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid ReplyDto replyDto) {
        Integer idx = authMemeberDto.getIdx();
        Long num = replyService.modify(idx, replyDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //댓글 삭제
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestParam @Min(1) Long num) {
        Integer idx = authMemeberDto.getIdx();
        replyService.remove(idx, num);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
