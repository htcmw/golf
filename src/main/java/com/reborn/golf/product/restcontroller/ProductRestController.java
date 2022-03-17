package com.reborn.golf.product.restcontroller;


import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.product.dto.ProductDto;
import com.reborn.golf.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductRestController {
    private final ProductService productService; //final

    // 제품 리스트 조회
    @GetMapping("/categories/{categoryCode}/products")
    public ResponseEntity<PageResultDto<Object[], ProductDto>> getList(@PathVariable(required = false) String categoryCode,
                                                                       @RequestParam(required = false) String attribute,
                                                                       @ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(productService.getList(categoryCode, attribute, pageRequestDto));
    }

    // 제품 상세 페이지
    @GetMapping("/products/{productIdx}")
    public ResponseEntity<ProductDto> read(@PathVariable Long productIdx) {
        return ResponseEntity.ok(productService.detail(productIdx));
    }

    // 제품 등록
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/categories/{categoryIdx}/products")
    public ResponseEntity<Long> register(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.register(productDto));
    }

    // 제품 정보 수정 (텍스트 수정 확인, 이미지 수정 테스트 픽요)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/products/{productIdx}")
    public ResponseEntity<Long> modify(@PathVariable Long productIdx, @RequestBody ProductDto productDto) {
        productService.modify(productIdx, productDto);
        return ResponseEntity.ok(productIdx);
    }

    // 제품 정보 삭제
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/products/{productIdx}")
    public ResponseEntity<Long> remove(@PathVariable Long productIdx) {
        productService.remove(productIdx);
        return ResponseEntity.ok(productIdx);
    }
}
