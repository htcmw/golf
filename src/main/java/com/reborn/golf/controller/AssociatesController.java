package com.reborn.golf.controller;

import com.reborn.golf.dto.AssociatesDto;
import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.AssociatesService;
import com.reborn.golf.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/associates")
public class AssociatesController {

    private final AssociatesService associatesService;

    //직원 입사
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> register(@RequestBody @Valid AssociatesDto associatesDto) {
        log.info(associatesDto);
        if (associatesService.register(associatesDto)) {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The same email already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    //직원 정보 조회
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<AssociatesDto> read(@AuthenticationPrincipal AuthMemeberDto authMemeberDto) {
        Integer idx = authMemeberDto.getIdx();
        log.info(idx);
        AssociatesDto associatesDto = associatesService.read(idx);
        return new ResponseEntity<>(associatesDto, HttpStatus.OK);
    }

    //직원 정보 수정
    @PutMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid AssociatesDto associatesDto) throws Exception {
        Integer idx = authMemeberDto.getIdx();
        associatesService.modify(idx, associatesDto);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

    //퇴사
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto) {
        Integer idx = authMemeberDto.getIdx();
        associatesService.remove(idx);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }



}
