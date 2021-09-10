package com.reborn.golf.controller;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public ResponseEntity<PageResultDto<Notice, NoticeDto>> getList(PageRequestDto pageRequestDto) {
        PageResultDto<Notice, NoticeDto> noticeDtoList = noticeService.getList(pageRequestDto);
        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    @PostMapping("/list.role")
    public ResponseEntity<PageResultDto<Notice,NoticeDto>> getListWithEmail(PageRequestDto pageRequestDto, @RequestBody Map<String,String> param){
        PageResultDto<Notice,NoticeDto> pageResultDto = noticeService.getListByEmail(pageRequestDto, param.get("email"));
        return new ResponseEntity<>(pageResultDto,HttpStatus.OK);
    }

    @PostMapping("/register.role")
    public ResponseEntity<Long> register(@RequestBody NoticeDto noticeDto) {
        Long num = noticeService.register(noticeDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }


    @GetMapping("/read/{num}")
    public ResponseEntity<NoticeDto> read(@PathVariable Long num) {
        NoticeDto noticeDto = noticeService.read(num);
        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    @PutMapping("/modify/{num}.role")
    public ResponseEntity<String> modify(@RequestBody NoticeDto noticeDto, @PathVariable Long num) {
        noticeService.modify(noticeDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @DeleteMapping("/remove/{num}.role")
    public ResponseEntity<String> remove(@PathVariable Long num) {
        noticeService.remove(num);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
