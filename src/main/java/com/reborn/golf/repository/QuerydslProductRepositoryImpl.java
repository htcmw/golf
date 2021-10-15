package com.reborn.golf.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.reborn.golf.entity.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class QuerydslProductRepositoryImpl extends QuerydslRepositorySupport implements QuerydslProductRepository {
    public QuerydslProductRepositoryImpl() {
        super(Product.class);
    }

    @Override
    public Page<Object[]> getProductList(String categoryCode, Pageable pageable) {
        QCategory category = QCategory.category;
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QProductReply productReply = QProductReply.productReply;

        JPQLQuery<Product> jpqlQuery = from(product);
        jpqlQuery.leftJoin(category).on(product.category.eq(category));
        jpqlQuery.leftJoin(productImage).on(productImage.product.eq(product));
        jpqlQuery.leftJoin(productReply).on(productReply.product.eq(product));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(product, productImage, productReply.grade.avg().coalesce(0.0), productReply.countDistinct());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(product.category.code.startsWith(categoryCode));
        booleanBuilder.and(product.removed.isFalse());
        booleanBuilder.and(productImage.removed.isFalse());

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Product.class, "product");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(product);
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        Long count = tuple.fetchCount();

        log.info("Count : " + count);

        return new PageImpl<>(result.stream().map(Tuple::toArray).collect(Collectors.toList()), pageable, count);
//        return new PageImpl<>(result.stream().map(Tuple::toArray).collect(Collectors.toList()), pageable, count);
    }


}
