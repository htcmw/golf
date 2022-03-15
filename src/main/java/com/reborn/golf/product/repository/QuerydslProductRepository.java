package com.reborn.golf.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuerydslProductRepository {
    Page<Object[]> getProductList(String categoryCode, Pageable pageable);

    }