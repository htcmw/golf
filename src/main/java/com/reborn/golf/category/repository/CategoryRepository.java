package com.reborn.golf.category.repository;

import com.reborn.golf.category.dto.CategoryDto;
import com.reborn.golf.category.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    //getList
    @Query("SELECT new com.reborn.golf.category.dto.CategoryDto(c.idx, c.parent.idx, c.name, c.code, c.priority) " +
            "FROM Category c " +
            "WHERE c.removed = false")
    List<CategoryDto> findAllByRemovedFalse(Sort sort);

    Optional<Category> findByNameAndRemovedFalse(String categoryName);

    @Query("SELECT COUNT(c) " +
            "FROM Category c " +
            "WHERE c.parent.idx is null")
    Integer countPidIsNUll();

    @Query("SELECT COUNT(c) " +
            "FROM Category c " +
            "WHERE c.parent.idx = :pid")
    Integer countChildren(@Param("pid") Integer pid);

    Category getByIdxAndAndRemovedFalse(Integer idx);

    @Query(value = "SELECT c " +
            "FROM Category c " +
            "WHERE c.code LIKE CONCAT((SELECT sc.code FROM Category sc WHERE sc.idx = :idx),'%')"
            , nativeQuery = true)
    List<Category> findAllAssociatedWithIdx(@Param("idx") Integer idx);

}
