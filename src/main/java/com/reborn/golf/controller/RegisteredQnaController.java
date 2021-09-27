package com.reborn.golf.controller;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registered-question")
@RequiredArgsConstructor
public class RegisteredQnaController {
    @GetMapping
    public ResponseBody getList(PageRequestDto pageRequestDto){
        return null;
    }

    @GetMapping("/{num}")
    public ResponseBody read(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long num){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseBody register(){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping
    public ResponseBody modify(){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{num}")
    public ResponseBody remove(@PathVariable Long num){
        return null;
    }
}
