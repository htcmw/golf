package com.reborn.golf.product.repository;

import com.reborn.golf.product.entity.Product;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Log4j2
public class QuerydslProductRepositoryImpl extends QuerydslRepositorySupport implements QuerydslProductRepository {
    public QuerydslProductRepositoryImpl() {
        super(Product.class);
    }

    @Override
    public Page<Object[]> getProductList(String categoryCode, Pageable pageable) {
//        QCategory category = QCategory.category;
//        QCategory category1 = new QCategory("category1");
//        QProduct product = QProduct.product;
//        QProductImage productImage = QProductImage.productImage;
//        QProductReply productReply = QProductReply.productReply;
//
//        JPQLQuery<Product> jpqlQuery = from(product);
//        jpqlQuery.leftJoin(category).on(product.category.eq(category1));
//        jpqlQuery.leftJoin(productImage).on(productImage.product.eq(product));
//        jpqlQuery.leftJoin(productReply).on(productReply.product.eq(product));
//
//        JPQLQuery<Tuple> tuple = jpqlQuery.select(product, productImage, productReply.grade.avg().coalesce(0.0), productReply.countDistinct());
//
//        BooleanBuilder booleanBuilder = new BooleanBuilder();
//
//        //categoryCode가 "01"이면 BEST 카테고리이므로 categoryCode를 검사하지 않는다
//        if(!(categoryCode.equals("01")))
//            booleanBuilder.and(product.category.code.startsWith(categoryCode));
//
//        booleanBuilder.and(product.removed.isFalse());
//        booleanBuilder.and(productImage.removed.isFalse());
//
//        tuple.where(booleanBuilder);
//
//        Sort sort = pageable.getSort();
//
//        sort.stream().forEach(order -> {
//            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
//            String prop = order.getProperty();
//
//            PathBuilder orderByExpression = new PathBuilder(Product.class, "product");
//            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
//        });
//
//        tuple.groupBy(product);
//        tuple.offset(pageable.getOffset());
//        tuple.limit(pageable.getPageSize());
//
//        List<Tuple> result = tuple.fetch();
//
//        log.info(result);
//
//        Long count = tuple.fetchCount();
//
//        log.info("Count : " + count);
//
//        return new PageImpl<>(result.stream().map(Tuple::toArray).collect(Collectors.toList()), pageable, count);
//        return new PageImpl<>(result.stream().map(Tuple::toArray).collect(Collectors.toList()), pageable, count);
        return null;
    }


}
