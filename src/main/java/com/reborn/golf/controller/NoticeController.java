package com.reborn.golf.controller;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/*
* */
@Log4j2
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    //모든 공지사항 목록을 출력
    @GetMapping("/list")
    public ResponseEntity<PageResultDto<Notice, NoticeDto>> getList(PageRequestDto pageRequestDto) {
        PageResultDto<Notice, NoticeDto> noticeDtoList = noticeService.getList(pageRequestDto);
        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    //Map<String, String> param은 json의 key를 email로 해주세요
    //작성자 이메일의 공지사항 목록 출력
    @PostMapping("/list.role")
    public ResponseEntity<PageResultDto<Object[], NoticeDto>> getListWithEmail(PageRequestDto pageRequestDto, @RequestBody Map<String, String> param) {
        PageResultDto<Object[], NoticeDto> pageResultDto = noticeService.getListByEmail(pageRequestDto, param.get("email"));
        return new ResponseEntity<>(pageResultDto, HttpStatus.OK);
    }

    //공지사항 등록
    @PostMapping("/register.role")
    public ResponseEntity<Long> register(@RequestBody NoticeDto noticeDto) {
        Long num = noticeService.register(noticeDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //공지사항 조회
    @GetMapping("/read/{num}")
    public ResponseEntity<NoticeDto> read(@PathVariable Long num) {
        NoticeDto noticeDto = noticeService.read(num);
        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    //공지사항 수정
    @PutMapping("/modify.role")
    public ResponseEntity<String> modify(@RequestBody NoticeDto noticeDto) {
        try {
            noticeService.modify(noticeDto);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
        }
    }

    //공지사항 삭제
    @DeleteMapping("/remove/{num}.role")
    public ResponseEntity<String> remove(@PathVariable Long num) {
        noticeService.remove(num);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
