package com.reborn.golf.controller;


import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ProductDto;
import com.reborn.golf.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService; //final

    // 상품 등록 관련 페이지
    @GetMapping("/register")
    public void register(){
    }

    // 제품 리스트 조회
    @GetMapping
    public ResponseEntity<PageResultDto<Object[], ProductDto>> getList(PageRequestDto requestDto) {

        PageResultDto<Object[], ProductDto> productDtoList = productService.getList(requestDto);

        log.info(productDtoList);

        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }
//
    // 제품 등록
    @PostMapping(value = "/register")
    public ResponseEntity<Long> register(ProductDto productDto) {

        log.info("productDto: " + productDto);

        Long pno = productService.register(productDto);

        return new ResponseEntity<>(pno, HttpStatus.OK);
    }

    // 제품 상세 페이지
    @GetMapping("/{pno}")
    public ResponseEntity<ProductDto> detail(@PathVariable Long pno) {

        ProductDto productDto = productService.detail(pno);

        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    // 제품 정보 수정 (미완성)
    @PutMapping(value = "/modify")
    public ResponseEntity<String> modify(@RequestBody Long pno, ProductDto productDto) {

        productService.modify(pno, productDto);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // 제품 정보 삭제
    @DeleteMapping("/{pno}")
    public ResponseEntity<String> remove(@PathVariable Long pno) {
        productService.remove(pno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
