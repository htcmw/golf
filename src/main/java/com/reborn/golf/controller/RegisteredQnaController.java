package com.reborn.golf.controller;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.RegisteredQnaDto;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.RegisteredQnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registered-question")
@RequiredArgsConstructor
public class RegisteredQnaController {

    private final RegisteredQnaService registeredQnaService;

    @GetMapping
    public ResponseEntity<PageResultDto<Object[], RegisteredQnaDto>> getList(PageRequestDto pageRequestDto) {
        PageResultDto<Object[], RegisteredQnaDto> registeredQnaDtos = registeredQnaService.getList(pageRequestDto);
        return new ResponseEntity<>(registeredQnaDtos, HttpStatus.OK);
    }

    @GetMapping("/{idx}")
    public ResponseEntity<RegisteredQnaDto> read(@PathVariable Long idx) {
        RegisteredQnaDto registeredQnaDto = registeredQnaService.read(idx);
        return new ResponseEntity<>(registeredQnaDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, RegisteredQnaDto registeredQnaDto) {
        Long idx = registeredQnaService.Register(authMemeberDto.getIdx(), registeredQnaDto);
        return new ResponseEntity<>(idx, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<String> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, RegisteredQnaDto registeredQnaDto) {
        registeredQnaService.modify(authMemeberDto.getIdx(), registeredQnaDto);
        return new ResponseEntity<>("Modification was successful", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx) {
        registeredQnaService.remove(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>("Deletion was successful", HttpStatus.OK);
    }


}
