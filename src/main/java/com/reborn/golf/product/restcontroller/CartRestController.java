package com.reborn.golf.product.restcontroller;

import com.reborn.golf.product.dto.CartDto;
import com.reborn.golf.product.dto.CartListDto;
import com.reborn.golf.product.service.CartService;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@Log4j2
@RequiredArgsConstructor
public class CartRestController {
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
        log.info(cartDto);
        Long cartIdx = cartService.register(memberIdx, cartDto);

        return new ResponseEntity<>(cartIdx, HttpStatus.OK);
    }

    @PutMapping("/{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx,@RequestParam Integer quentity){
        Integer memberIdx = authMemeberDto.getIdx();
        Long cartIdx = cartService.modify(memberIdx, idx, quentity);

        return new ResponseEntity<>(cartIdx, HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx){
        Integer memberIdx = authMemeberDto.getIdx();
        cartService.remove(memberIdx,idx);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> removeAll(@AuthenticationPrincipal AuthMemeberDto authMemeberDto){
        Integer memberIdx = authMemeberDto.getIdx();
        cartService.removeAll(memberIdx);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
