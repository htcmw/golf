package com.reborn.golf.controller;

import com.reborn.golf.dto.user.EmployeeDto;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.EmployeeService;
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
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    //직원 입사
    @PostMapping
//    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> register(@RequestBody @Valid EmployeeDto employeeDto) {
        log.info(employeeDto);
        if (employeeService.register(employeeDto)) {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The same email already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    //직원 정보 조회
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<EmployeeDto> read(@AuthenticationPrincipal AuthMemeberDto authMemeberDto) {
        Integer idx = authMemeberDto.getIdx();
        log.info(idx);
        EmployeeDto employeeDto = employeeService.read(idx);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    //직원 정보 수정
    @PutMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid EmployeeDto employeeDto) throws Exception {
        Integer idx = authMemeberDto.getIdx();
        employeeService.modify(idx, employeeDto);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

    //퇴사
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto) {
        Integer idx = authMemeberDto.getIdx();
        employeeService.remove(idx);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
