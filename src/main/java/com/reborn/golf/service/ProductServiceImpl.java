package com.reborn.golf.service;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.exception.NotExistEntityException;
import com.reborn.golf.dto.shop.ProductDto;
import com.reborn.golf.dto.shop.ProductImageDto;
import com.reborn.golf.entity.*;
import com.reborn.golf.repository.CategoryRepository;
import com.reborn.golf.repository.ProductImageRepository;
import com.reborn.golf.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository; //final
    private final ProductImageRepository productImageRepository; //final

    @Override
    @Transactional
    public PageResultDto<Object[], ProductDto> getList(Integer categoryIdx, PageRequestDto requestDto) {

        Category category = categoryRepository.findById(categoryIdx)
                .orElseThrow(() -> new NotExistEntityException("카테고리 idx에 맞는 제품이 없습니다"));

        Page<Object[]> result = productRepository.getProductList(category.getCode(), requestDto.getPageable(Sort.by("salesVolume").descending()));

        //List.of DTO에 맞추기위해서 사용, 이미지는 한개
        Function<Object[], ProductDto> fn = (arr -> entitiesToDto((Product) arr[0], List.of((ProductImage) arr[1]), (Double) arr[2], (Long) arr[3]));

        return new PageResultDto<>(result, fn);
    }

    @Override
    public List<ProductDto> getBestList(String attribute, Integer limit) {
        List<Object[]> result = productRepository.getBestProducts(PageRequest.of(0,limit,Sort.by(attribute).descending()));
        return result.stream().map(arr -> entitiesToDto((Product) arr[0], List.of((ProductImage) arr[1]), (Double) arr[2], (Long) arr[3])).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto detail(Long productIdx) {

        List<Object[]> result = productRepository.getProductWithAll(productIdx);

        Product product = (Product) result.get(0)[0];

        List<ProductImage> productImageList = new ArrayList<>();

        result.forEach(arr -> {
            ProductImage productImage = (ProductImage) arr[1];
            productImageList.add(productImage);
        });

        Double avg = (Double) result.get(0)[2];
        Long reviewCnt = (Long) result.get(0)[3];

        return entitiesToDto(product, productImageList, avg, reviewCnt);
    }

    @Override
    public Long register(ProductDto productDto) {

        Map<String, Object> entityMap = dtoToEntity(productDto);

        Product product = (Product) entityMap.get("product");

        List<ProductImage> productImageList = (List<ProductImage>) entityMap.get("imgList");

        productRepository.save(product);

        productImageList.forEach(productImage -> {
            productImageRepository.save(productImage);
        });

        return product.getIdx();
    }


    @Override
    public void modify(Long productIdx, ProductDto productDto) {
        Product product = productRepository.getProductByIdx(productIdx)
                .orElseThrow(() -> new NotExistEntityException("해당 idx를 갖는 제품이 DB에 없습니다"));

        //product 데이터 변경
        if (product.getIdx().equals(productDto.getIdx())) {
            product.changeCategory(productDto.getCategoryIdx());
            product.changeTitle(productDto.getTitle());
            product.changeBrand(productDto.getBrand());
            product.changeQuantity(productDto.getQuantity());
            product.changePrice(productDto.getPrice());
            product.changeContent(productDto.getContent());
            productRepository.save(product);

            //기존 이미지 삭제
            productImageRepository.findAllByProductIdx(productIdx).forEach(productImage -> {
                productImage.changeRemoved(true);
            });
            //수정 이미지 삽입
            productDto.getImageDtoList().forEach(productImageDto -> {
                ProductImage productImage = ProductImage.builder()
                        .path(productImageDto.getPath())
                        .imgName(productImageDto.getImgName())
                        .uuid(productImageDto.getUuid())
                        .product(product)
                        .build();
                productImageRepository.save(productImage);
            });

        }
    }
    @Override
    public void remove(Long productIdx) {
        Product product = productRepository.getProductByIdx(productIdx)
                .orElseThrow(() -> new NotExistEntityException("해당 idx를 갖는 제품이 DB에 없습니다"));

        //제품이미지 삭제
        product.getProductImages().forEach(productImage -> {
            productImage.changeRemoved(true);
        });
        //제품 삭제
        product.changeRemoved(true);
        productRepository.save(product);
    }

}
