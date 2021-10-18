package com.reborn.golf.controller;


import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.ProductDto;
import com.reborn.golf.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService; //final

    // 제품 리스트 조회
    @GetMapping("/categories/{categoryIdx}/products")
    public ResponseEntity<PageResultDto<Object[], ProductDto>> getList(@PathVariable Integer categoryIdx, PageRequestDto requestDto) {
        log.info(categoryIdx);
        PageResultDto<Object[], ProductDto> productDtoList = productService.getList(categoryIdx, requestDto);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }
    // 제품 리스트 조회
    @GetMapping("/products/best")
    public ResponseEntity<List<ProductDto>> getBestList(@RequestParam String attr, @RequestParam Integer limit) {

        List<ProductDto> productDtoList = productService.getBestList(attr, limit);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }
    // 제품 상세 페이지
    @GetMapping("/products/{productIdx}")
    public ResponseEntity<ProductDto> read(@PathVariable Long productIdx) {
        ProductDto productDto = productService.detail(productIdx);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    // 제품 등록
    @PostMapping("/categories/{categoryIdx}/products")
    public ResponseEntity<Long> register(@RequestBody ProductDto productDto) {
        log.info(productDto);
        Long pno = productService.register(productDto);
        return new ResponseEntity<>(pno, HttpStatus.OK);
    }


    // 제품 정보 수정 (텍스트 수정 확인, 이미지 수정 테스트 픽요)
    @PutMapping("/products/{productIdx}")
    public ResponseEntity<String> modify(@PathVariable Long productIdx, @RequestBody ProductDto productDto) {
        productService.modify(productIdx, productDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 제품 정보 삭제
    @DeleteMapping("/products/{productIdx}")
    public ResponseEntity<String> remove(@PathVariable Long productIdx) {
        productService.remove(productIdx);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
