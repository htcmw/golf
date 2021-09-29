package com.reborn.golf.service;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.ProductDto;
import com.reborn.golf.dto.ProductPageResultDto;
import com.reborn.golf.entity.*;
import com.reborn.golf.repository.ProductImageRepository;
import com.reborn.golf.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
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

    private final ProductRepository productRepository; //final

    private final ProductImageRepository productImageRepository; //final

    @Override
    @Transactional
    public ProductPageResultDto<ProductDto, Object[]> getList(PageRequestDto requestDto) {
        Pageable pageable = requestDto.getPageable(Sort.by("pno").descending());
        Page<Object[]> result = productRepository.getListPage(pageable);

        log.info("==============================================");
        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));
        });

        Function<Object[], ProductDto> fn = (arr -> entitiesToDTO(
                (Product) arr[0] ,
                (List<ProductImage>)(Arrays.asList((ProductImage)arr[1])),
                (Double) arr[2],
                (Long)arr[3])
        );

        return new ProductPageResultDto<>(result, fn);
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

        return product.getPno();
    }

    @Override
    @Transactional
    public ProductDto detail(Long pno) {

        List<Object[]> result = productRepository.getProductWithAll(pno);

        Product product = (Product) result.get(0)[0];

        List<ProductImage> productImageList = new ArrayList<>();

        result.forEach(arr -> {
            ProductImage  productImage = (ProductImage)arr[1];
            productImageList.add(productImage);
        });

        Double avg = (Double) result.get(0)[2];
        Long reviewCnt = (Long) result.get(0)[3];

        return entitiesToDTO(product, productImageList, avg, reviewCnt);
    }

    @Override
    public void remove(Long pno) {
        Optional<Product> result = productRepository.getProductByPno(pno);
        if (result.isPresent()) {
            Product product = result.get();

            product.changeRemoved(true);
            productRepository.save(product);
        }
    }

    @Override
    public void modify(Long pno, ProductDto productDto) {

        Optional<Product> result = productRepository.getProductByPno(pno);
        Map<String, Object> entityMap = dtoToEntity(productDto);
        List<ProductImage> productImageList = (List<ProductImage>) entityMap.get("imgList");

        if (result.isPresent()) {
            Product product = result.get();
                product.changeTitle(productDto.getTitle());
                product.changeBrand(productDto.getBrand());
                product.changeRank(productDto.getRank());
                product.changeQuentity(productDto.getQuentity());
                product.changePrice(productDto.getPrice());
                product.changeContent(productDto.getContent());

                log.info(product);
                productRepository.save(product);

                productImageList.forEach(productImage -> {
                    productImageRepository.save(productImage);
                });
        }
    }
}
