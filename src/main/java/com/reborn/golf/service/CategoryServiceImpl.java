package com.reborn.golf.service;

import com.reborn.golf.controller.CategoryDto;
import com.reborn.golf.entity.Category;
import com.reborn.golf.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getList() {
        List<Category> categories = categoryRepository.findCategoriesByRemovedFalse(Sort.by("name").ascending());
        return categories.stream().map(this::entiryToDto).collect(Collectors.toList());
    }

    @Override
    public Integer register(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.getCategoryByName(categoryName);
        Category category;
        if(optionalCategory.isEmpty()){
            category = Category.builder().name(categoryName).build();
            categoryRepository.save(category);
        }
        else{
            category = optionalCategory.get();
            category.changeRemoved(true);
            categoryRepository.save(category);
        }
        return category.getIdx();
    }

    @Override
    public Integer modify(CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getIdx());

        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.changeName(categoryDto.getName());
            categoryRepository.save(category);
            return category.getIdx();
        }
        return null;
    }

    @Override
    public Integer remove(Integer categoryIdx) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryIdx);

        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.changeRemoved(true);
            categoryRepository.save(category);
            return category.getIdx();
        }
        return null;
    }
}
