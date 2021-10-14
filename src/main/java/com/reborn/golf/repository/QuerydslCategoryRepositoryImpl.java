package com.reborn.golf.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.reborn.golf.entity.Category;
import com.reborn.golf.entity.QCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class QuerydslCategoryRepositoryImpl extends QuerydslRepositorySupport implements QuerydslCategoryRepository{
    public QuerydslCategoryRepositoryImpl() {
        super(Category.class);
    }

    @Override
    public List<Category> getCategories() {
        QCategory parent =new QCategory("parent");
        QCategory child =new QCategory("child");

        JPQLQuery<Category> from = from(parent)
                .leftJoin(parent.children, child)
                .fetchJoin()
                .where(parent.parent.isNull())
                .orderBy(parent.priority.asc(), child.priority.asc());

        JPQLQuery<Category> categoryJPQLQuery = from.select(parent).distinct();

        return categoryJPQLQuery.fetch();
    }
}
