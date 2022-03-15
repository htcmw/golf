package com.reborn.golf.customerservice.restcontroller;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.customerservice.dto.CustomerserviceType;
import com.reborn.golf.customerservice.dto.NoticeDto;
import com.reborn.golf.customerservice.entity.Notice;
import com.reborn.golf.customerservice.service.CustomerserviceService;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@RequestMapping("/api/notices")
@Validated
@RequiredArgsConstructor
public class NoticeRestController {

    private final CustomerserviceService customerserviceService;
    private final CustomerserviceType csType = CustomerserviceType.NOTICE;

    //모든 공지사항 목록을 출력
    @GetMapping
    public ResponseEntity<PageResultDto<Notice, NoticeDto>> getList(PageRequestDto pageRequestDto) throws IllegalAccessException {
        return ResponseEntity.ok(customerserviceService.getList(pageRequestDto, csType));
    }

    //공지사항 조회
    @GetMapping("/{idx}")
    public ResponseEntity<NoticeDto> read(@PathVariable @Min(1) Long idx) {
        return ResponseEntity.ok((NoticeDto)customerserviceService.read(idx, csType));
    }

    //공지사항 등록
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid NoticeDto noticeDto) {
        return ResponseEntity.ok(customerserviceService.register(authMemeberDto.getIdx(), noticeDto));
    }

    //공지사항 수정
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid NoticeDto noticeDto) {
        customerserviceService.modify(authMemeberDto.getIdx(), noticeDto);
        return ResponseEntity.ok(noticeDto.getIdx());
    }

    //공지사항 삭제
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable @Min(1) Long idx) {
        customerserviceService.remove(authMemeberDto.getIdx(), idx);
        return ResponseEntity.ok(idx);
    }
}
