package com.reborn.golf.service;

import com.reborn.golf.dto.exception.ImpossibleDeleteException;
import com.reborn.golf.dto.shop.CategoryDto;
import com.reborn.golf.dto.exception.AlreadyExistEntityException;
import com.reborn.golf.dto.exception.NotExistEntityException;
import com.reborn.golf.entity.Category;
import com.reborn.golf.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getList() {
        List<Category> categories = categoryRepository.getCategories();
        return categories.stream().map(this::entiryToDto).collect(Collectors.toList());
    }


    @Override
    public Integer register(CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.getCategoryByName(categoryDto.getName());
        if (optionalCategory.isEmpty()) {
            Category category = Category.builder()
                    .name(categoryDto.getName())
                    .priority(categoryDto.getPriority())
                    .build();
            if (categoryDto.getPidx() != null) {
                Category parent = categoryRepository.findById(categoryDto.getPidx())
                        .orElseThrow(() -> new NotExistEntityException(categoryDto.getPidx() + "의 상위 카테고리가 없습니다"));
                category.setParent(parent);
                category.setCode(parent.getCode() + categoryDto.getCode());
            }
            categoryRepository.save(category);
            return category.getIdx();
        } else {
            throw new AlreadyExistEntityException("이미 같은 이름의 카테고리가 있습니다");
        }
    }

    @Override
    public Integer modify(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getIdx())
                .orElseThrow(() -> new NotExistEntityException(categoryDto.getIdx() + "의 카테고리가 없습니다"));

        category.changeCode(categoryDto.getCode());
        category.changeName(categoryDto.getName());
        category.changePriority(categoryDto.getPriority());
        category.changeName(categoryDto.getName());
        categoryRepository.save(category);
        return category.getIdx();
    }

    @Override
    public void remove(Integer categoryIdx) {
        try{
            categoryRepository.deleteById(categoryIdx);
        }catch (DataIntegrityViolationException e){
            throw new ImpossibleDeleteException(e.getMessage());
        }
    }

}
