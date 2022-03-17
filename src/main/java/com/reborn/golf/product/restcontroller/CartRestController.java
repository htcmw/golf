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
    public ResponseEntity<CartListDto> getList(@AuthenticationPrincipal AuthMemeberDto authMemeberDto) {
        return ResponseEntity.ok(cartService.getList(authMemeberDto.getIdx()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> register(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody CartDto cartDto) {
        return ResponseEntity.ok(cartService.register(authMemeberDto.getIdx(), cartDto));
    }

    @PutMapping("/{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> modify(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx, @RequestParam Integer quentity) {
        cartService.modify(authMemeberDto.getIdx(), idx, quentity);
        return ResponseEntity.ok(idx);
    }

    @DeleteMapping("/{idx}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable Long idx) {
        cartService.remove(authMemeberDto.getIdx(), idx);
        return ResponseEntity.ok(idx);
    }

    @DeleteMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> removeAll(@AuthenticationPrincipal AuthMemeberDto authMemeberDto) {
        cartService.removeAll(authMemeberDto.getIdx());
        return ResponseEntity.ok("all");
    }


}
