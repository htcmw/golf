package com.reborn.golf.service;

import com.reborn.golf.dto.ProductDto;
import com.reborn.golf.entity.Product;
import com.reborn.golf.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long saveFile(ProductDto productDto) {
        return productRepository.save(productDto.toEntity()).getId();
    }

    @Transactional
    public ProductDto getFile(Long id) {
        Product product = productRepository.findById(id).get();

       ProductDto productDto = ProductDto.builder()
               .Id(product.getId())
               .title(product.getTitle())
               .brand(product.getBrand())
               .rank(product.getRank())
               .quentity(product.getQuentity())
               .price(product.getPrice())
               .content(product.getContent())
               .origFilename(product.getOrigFilename())
               .filename(product.getFilename())
               .filePath(product.getFilePath())
               .build();
       return productDto;
    }
}
