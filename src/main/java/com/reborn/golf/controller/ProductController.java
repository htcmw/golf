package com.reborn.golf.controller;


import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.ProductDto;
import com.reborn.golf.dto.ProductPageResultDto;
import com.reborn.golf.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/shop")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService; //final

    // 제품 리스트 조회
    @GetMapping
    public ResponseEntity<ProductPageResultDto<ProductDto, Object[]>> getList(PageRequestDto requestDto) {

        ProductPageResultDto<ProductDto, Object[]> productDtoList = productService.getList(requestDto);

        log.info(productDtoList);

        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    // 제품 등록
    @PostMapping
    public ResponseEntity<Long> register(@RequestBody ProductDto productDto) {

        log.info("productDto: " + productDto);

        Long pno = productService.register(productDto);

        return new ResponseEntity<>(pno, HttpStatus.OK);
    }

    // 제품 상세 페이지
    @GetMapping(value = "/{pno}")
    public ResponseEntity<ProductDto> detail(@PathVariable Long pno) {

        ProductDto productDto = productService.detail(pno);

        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    // 제품 정보 수정 (텍스트 수정 확인, 이미지 수정 테스트 픽요)
    @PutMapping(value = "/{pno}")
    public ResponseEntity<String> modify(@PathVariable Long pno, @RequestBody ProductDto productDto) {

        productService.modify(pno, productDto);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // 제품 정보 삭제
    @DeleteMapping(value = "/{pno}")
    public ResponseEntity<String> remove(@PathVariable Long pno) {
        productService.remove(pno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
