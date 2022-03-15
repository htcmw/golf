package com.reborn.golf.customerservice.restcontroller;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.customerservice.dto.CustomerserviceType;
import com.reborn.golf.customerservice.dto.KnownQnaDto;
import com.reborn.golf.customerservice.entity.KnownQna;
import com.reborn.golf.customerservice.service.CustomerserviceService;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/knownQna")
@RequiredArgsConstructor
public class KnownQnaRestController {

    private final CustomerserviceService customerserviceService;
    private final CustomerserviceType csType = CustomerserviceType.KNOWN_QNA;
    @GetMapping
    public ResponseEntity<PageResultDto<KnownQna, KnownQnaDto>> getList(PageRequestDto pageRequestDto) throws IllegalAccessException {
        return ResponseEntity.ok(customerserviceService.getList(pageRequestDto, csType));
    }

    @GetMapping("/{idx}")
    public ResponseEntity<KnownQnaDto> read(@PathVariable Long idx) {
        return ResponseEntity.ok((KnownQnaDto)customerserviceService.read(idx, csType));
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                         @RequestBody KnownQnaDto knownQnaDto) {
        return ResponseEntity.ok(customerserviceService.register(authMemeberDto.getIdx(), knownQnaDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                       @RequestBody KnownQnaDto knownQnaDto) {
        customerserviceService.modify(authMemeberDto.getIdx(), knownQnaDto);
        return ResponseEntity.ok(knownQnaDto.getIdx());
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                       @PathVariable Long idx) {
        customerserviceService.remove(authMemeberDto.getIdx(), idx);
        return ResponseEntity.ok(idx);
    }
}
