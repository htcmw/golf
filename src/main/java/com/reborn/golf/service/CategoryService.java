package com.reborn.golf.service;

import com.reborn.golf.dto.shop.CategoryDto;
import com.reborn.golf.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public interface CategoryService {
    List<CategoryDto> getList();
    Integer register(CategoryDto categoryDto);
    Integer modify(CategoryDto categoryDto);
    void remove(Integer categoryIdx);


    default CategoryDto entiryToDto(Category category){
        return CategoryDto.builder()
                .idx(category.getIdx())
                .name(category.getName())
                .code(category.getCode())
                .priority(category.getPriority())
                .categories(category.getChildren().stream().map(this::entiryToDto).collect(Collectors.toList()))
                .build();
    }
    default Category dtoToEntiry(CategoryDto categoryDto){
        return Category.builder()
                .idx(categoryDto.getIdx())
                .name(categoryDto.getName())
                .build();
    }
}
