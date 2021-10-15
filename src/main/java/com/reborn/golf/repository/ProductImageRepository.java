package com.reborn.golf.repository;

import com.reborn.golf.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProductIdx(Long ProductIdx);
}
