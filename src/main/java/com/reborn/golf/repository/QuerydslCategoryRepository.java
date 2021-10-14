package com.reborn.golf.repository;

import com.reborn.golf.entity.Category;

import java.util.List;

public interface QuerydslCategoryRepository {
    List<Category> getCategories();
}
