package com.reborn.golf.repository;

import com.querydsl.jpa.JPQLQuery;
import com.reborn.golf.entity.Category;
import com.reborn.golf.entity.QCategory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class QuerydslCategoryRepositoryImpl extends QuerydslRepositorySupport implements QuerydslCategoryRepository{
    public QuerydslCategoryRepositoryImpl() {
        super(Category.class);
    }

    public List<Category> findAllWithQuerydsl() {
        QCategory parent =new QCategory("parent");
        QCategory child =new QCategory("child");

        JPQLQuery<Category> jpqlQuery = from(parent).distinct()
                .leftJoin(parent.children, child)
                .fetchJoin()
                .where(parent.parent.isNull())
                .orderBy(parent.orderByNumber.asc(), child.orderByNumber.asc());
        return jpqlQuery.fetch();
    }
}
