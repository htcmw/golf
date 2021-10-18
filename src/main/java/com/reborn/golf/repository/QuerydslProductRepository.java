package com.reborn.golf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface QuerydslProductRepository {
    Page<Object[]> getProductList(String categoryCode, Pageable pageable);

    }