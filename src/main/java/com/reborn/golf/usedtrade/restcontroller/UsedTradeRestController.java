package com.reborn.golf.usedtrade.restcontroller;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.usedtrade.dto.UsedTradeDto;
import com.reborn.golf.usedtrade.service.UsedTradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/purchased-products")
@RestController
public class UsedTradeRestController {

    private final UsedTradeService usedTradeService;

    //유저의 판매 리스트
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<PageResultDto<Object[], UsedTradeDto>> getListWithUser(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, PageRequestDto requestDto) {
        PageResultDto<Object[], UsedTradeDto> purchasedItemsDtoList = usedTradeService.getListWithUser(authMemeberDto.getIdx(), requestDto);
        log.info(purchasedItemsDtoList);
        return new ResponseEntity<>(purchasedItemsDtoList, HttpStatus.OK);
    }

    //유저의 판매 물품 디테일
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/{idx}")
    public ResponseEntity<UsedTradeDto> read(@PathVariable Long idx) {
        UsedTradeDto usedTradeDto = usedTradeService.read(idx);
        return new ResponseEntity<>(usedTradeDto, HttpStatus.OK);
    }

    //중고 용품 등록
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<UsedTradeDto> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                                 @RequestParam Integer categoryIdx,
                                                 @RequestBody UsedTradeDto usedTradeDto) {
        UsedTradeDto register = usedTradeService.register(authMemeberDto.getIdx(), categoryIdx, usedTradeDto);
        return new ResponseEntity<>(register, HttpStatus.OK);
    }

    //판매 정보 수정
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                       @RequestParam Integer categoryIdx,
                                       @RequestBody UsedTradeDto usedTradeDto) {
        Long num = usedTradeService.modify(authMemeberDto.getIdx(), categoryIdx, usedTradeDto);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //판매 취소
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<String> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx) {
        usedTradeService.remove(authMemeberDto.getIdx(), idx);
        return new ResponseEntity<>("purchasedProduct is removed successfully", HttpStatus.OK);
    }

    //판매 상태 변경
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_USER')")
    @PutMapping("/{idx}/step")
    public ResponseEntity<Map<String, Object>> modifyState(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                                           @PathVariable Long idx,
                                                           @RequestBody(required = false) Map<String, Integer> param) {
        Integer cost = null;
        if (param != null)
            cost = param.get("cost");
        Collection<GrantedAuthority> authorities = authMemeberDto.getAuthorities();
        Set<String> roleSet = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        Map<String, Object> map = usedTradeService.modifyStep(idx, roleSet, cost);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    //매니저가 보는 유저 판매 리스트
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/users")
    public ResponseEntity<PageResultDto<Object[], UsedTradeDto>> getList(PageRequestDto requestDto) {
        PageResultDto<Object[], UsedTradeDto> purchasedItemsDtoList = usedTradeService.getList(requestDto);
        log.info(purchasedItemsDtoList);
        return new ResponseEntity<>(purchasedItemsDtoList, HttpStatus.OK);
    }
}
