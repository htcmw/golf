package com.reborn.golf.service;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.shop.ProductDto;
import com.reborn.golf.dto.shop.ProductImageDto;
import com.reborn.golf.entity.Product;
import com.reborn.golf.entity.ProductImage;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ProductService {

    // 제품 리스트 조회
    PageResultDto<Object[], ProductDto> getList(PageRequestDto requestDto);

    // 제품 등록
    Long register(ProductDto productDto);

    // 제품 디테일페이지
    ProductDto detail(Long pno);

    // 제품 정보 수정
    void modify(Long pno, ProductDto productDto);

    // 제품 정보 삭제
    void remove(Long pno);

    default ProductDto entityToDto(Product product, List<ProductImage> productImages, Double avg, Long reviewCnt) {
        ProductDto productDto = ProductDto.builder()
                .idx(product.getIdx())
                .title(product.getTitle())
                .brand(product.getBrand())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .content(product.getContent())
                .regDate(product.getRegDate())
                .modDate(product.getModDate())
                .build();

        List<ProductImageDto> productImageDtoList = productImages.stream().map(productImage -> ProductImageDto.builder().imgName(productImage.getImgName()).path(productImage.getPath()).uuid(productImage.getUuid()).build()).collect(Collectors.toList());

        productDto.setImageDtoList(productImageDtoList);
        productDto.setAvg(avg);
        productDto.setReviewCnt(reviewCnt.intValue());


        return productDto;

    }

    default Map<String, Object> dtoToEntity(ProductDto productDto) {

        Map<String, Object> entityMap = new HashMap<>();

        Product product = Product.builder()
                .idx(productDto.getIdx())
                .title(productDto.getTitle())
                .brand(productDto.getBrand())
                .quantity(productDto.getQuantity())
                .price(productDto.getPrice())
                .content(productDto.getContent())
                .build();

        entityMap.put("product", product);

        List<ProductImageDto> imageDtoList = productDto.getImageDtoList();

        //ProductImageDto 처리
        if (imageDtoList != null && imageDtoList.size() > 0) {
            List<ProductImage> productImageList = imageDtoList.stream().map(productImageDto -> {
                ProductImage productImage = ProductImage.builder()
                        .path(productImageDto.getPath())
                        .imgName(productImageDto.getImgName())
                        .uuid(productImageDto.getUuid())
                        .product(product)
                        .build();
                return productImage;
            }).collect(Collectors.toList());

            entityMap.put("imgList", productImageList);
        }
        return entityMap;
    }
}
