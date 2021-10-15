package com.reborn.golf.repository;

import com.reborn.golf.entity.Product;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class QuerydslProductRepositoryImpl extends QuerydslRepositorySupport implements QuerydslProductRepository {
    public QuerydslProductRepositoryImpl() {
        super(Product.class);
    }




}
