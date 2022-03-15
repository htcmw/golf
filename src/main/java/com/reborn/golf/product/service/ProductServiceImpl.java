package com.reborn.golf.product.service;

import com.reborn.golf.category.repository.CategoryRepository;
import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.common.exception.NotExistEntityException;
import com.reborn.golf.product.dto.ProductDto;
import com.reborn.golf.product.entity.Product;
import com.reborn.golf.product.entity.ProductImage;
import com.reborn.golf.product.repository.ProductImageRepository;
import com.reborn.golf.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository; //final
    private final ProductImageRepository productImageRepository; //final

    @Override
    public PageResultDto<Object[], ProductDto> getList(String categoryCode, String attribute, PageRequestDto requestDto) {
        if (attribute == null) attribute = "salesVolume";
        Page<Object[]> result = productRepository.getProductList(categoryCode, requestDto.getPageable(Sort.by(attribute).descending()));
        Function<Object[], ProductDto> fn = (arr -> entitiesToDto((Product) arr[0], List.of((ProductImage) arr[1]), (Double) arr[2], (Long) arr[3]));
        PageResultDto<Object[], ProductDto> pageResultDto = new PageResultDto<>(result, fn);
        System.out.println(pageResultDto);
        return pageResultDto;
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
