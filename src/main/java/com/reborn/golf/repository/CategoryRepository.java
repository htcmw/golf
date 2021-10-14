package com.reborn.golf.repository;

import com.reborn.golf.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer>, QuerydslCategoryRepository {
    List<Category> findCategoriesByRemovedFalse(Sort sort);
    Optional<Category> getCategoryByName(String categoryName);
}
