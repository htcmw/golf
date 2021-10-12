package com.reborn.golf.controller;
import com.reborn.golf.dto.shop.CartDto;
import com.reborn.golf.dto.shop.CartListDto;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Log4j2
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CartListDto> getList(@AuthenticationPrincipal AuthMemeberDto authMemeberDto){
        Integer memberIdx = authMemeberDto.getIdx();
        CartListDto cartDtos = cartService.getList(memberIdx);
        return new ResponseEntity<>(cartDtos, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody CartDto cartDto){
        Integer memberIdx = authMemeberDto.getIdx();
        Long cartIdx = cartService.register(memberIdx, cartDto);

        return new ResponseEntity<>(cartIdx, HttpStatus.OK);
    }

    @PutMapping("{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx,@RequestParam Integer quentity){
        Integer memberIdx = authMemeberDto.getIdx();
        Long cartIdx = cartService.modify(memberIdx, idx, quentity);
        return new ResponseEntity<>(cartIdx, HttpStatus.OK);

    }

    @DeleteMapping("{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        Integer memberIdx = authMemeberDto.getIdx();
        Long cartIdx = cartService.remove(memberIdx,idx);
        return new ResponseEntity<>(cartIdx, HttpStatus.OK);

    }



}
