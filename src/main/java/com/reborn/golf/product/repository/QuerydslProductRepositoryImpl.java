package com.reborn.golf.product.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reborn.golf.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.reborn.golf.category.entity.QCategory.category;
import static com.reborn.golf.product.entity.QProduct.*;
import static com.reborn.golf.product.entity.QProductImage.productImage;
import static com.reborn.golf.review.entity.QProductReply.*;

public class QuerydslProductRepositoryImpl extends QuerydslRepositorySupport implements QuerydslProductRepository {
    private final JPAQueryFactory queryFactory;

    public QuerydslProductRepositoryImpl(EntityManager em) {
        super(Product.class);
        queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<Object[]> getProductList(String categoryCode, Pageable pageable) {
        long count = queryFactory
                .select(product.count())
                .from(product)
                .where(product.removed.isFalse(), categoryCodeEq(categoryCode))
                .fetchCount();

        JPAQuery<Tuple> tuple = queryFactory
                .select(product, productImage, productReply.grade.avg().coalesce(0.0), productReply.countDistinct())
                .from(product)
                .leftJoin(product.category, category)
                .leftJoin(productImage).on(productImage.product.eq(product))
                .leftJoin(productReply).on(productReply.product.eq(product))
                .where(product.removed.isFalse(), productImage.removed.isFalse(), categoryCodeEq(categoryCode))
                .groupBy(product)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //sortby
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Product.class, "product");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        List<Object[]> collect = tuple.fetch().stream().map(Tuple::toArray).collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, count);
    }

    private BooleanExpression categoryCodeEq(String categoryCode) {
        return categoryCode.equals("01") ? null : product.category.code.startsWith(categoryCode);
    }
}
