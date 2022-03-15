package com.reborn.golf.product.service;

import com.reborn.golf.category.entity.Category;
import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.product.dto.ProductDto;
import com.reborn.golf.product.dto.ProductImageDto;
import com.reborn.golf.product.entity.Product;
import com.reborn.golf.product.entity.ProductImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ProductService {

    // 제품 리스트 조회
    PageResultDto<Object[], ProductDto> getList(String categoryCode, String attribute, PageRequestDto requestDto);
    // 제품 등록
    Long register(ProductDto productDto);
    // 제품 디테일페이지
    ProductDto detail(Long pno);
    // 제품 정보 수정
    void modify(Long pno, ProductDto productDto);
    // 제품 정보 삭제
    void remove(Long pno);

    default ProductDto entitiesToDto(Product product, List<ProductImage> productImages, Double avg, Long reviewCnt) {

        List<ProductImageDto> productImageDtoList = productImages.stream().map(productImage -> ProductImageDto.builder()
                .imgName(productImage.getImgName())
                .path(productImage.getPath())
                .uuid(productImage.getUuid())
                .build()).collect(Collectors.toList());

        ProductDto productDto = ProductDto.builder()
                .idx(product.getIdx())
                .categoryIdx(product.getCategory().getIdx())
                .title(product.getTitle())
                .brand(product.getBrand())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .salesVolume(product.getSalesVolume())
                .content(product.getContent())
                .avg(avg)
                .reviewCnt(reviewCnt)
                .imageDtoList(productImageDtoList)
                .regDate(product.getRegDate())
                .modDate(product.getModDate())
                .build();

        return productDto;
    }


    default Map<String, Object> dtoToEntity(ProductDto productDto) {

        Map<String, Object> entityMap = new HashMap<>();

        Product product = Product.builder()
                .category(Category.builder().idx(productDto.getCategoryIdx()).build())
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
