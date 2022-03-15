package com.reborn.golf.category.service;

import com.reborn.golf.category.dto.CategoryDto;
import com.reborn.golf.category.entity.Category;
import com.reborn.golf.category.repository.CategoryRepository;
import com.reborn.golf.common.exception.AlreadyExistEntityException;
import com.reborn.golf.common.exception.NotExistEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getList() {
        List<CategoryDto> categories = categoryRepository.findAllByRemovedFalse(Sort.by("priority").ascending());
        Map<Integer, List<CategoryDto>> mapList = new HashMap<>();
        for (CategoryDto category : categories) { //grouping
            List<CategoryDto> categoryDtos = mapList.getOrDefault(category.getPidx(), new ArrayList<>());
            categoryDtos.add(category);
            mapList.put(category.getPidx(), categoryDtos);
        }
        return makeCategoryDtoList(null, mapList);
    }

    private List<CategoryDto> makeCategoryDtoList(Integer pid, Map<Integer, List<CategoryDto>> mapList) {
        if (!mapList.containsKey(pid)) return null;
        List<CategoryDto> result = new ArrayList<>();
        for (CategoryDto categoryDto : mapList.get(pid)) {
            categoryDto.setCategories(makeCategoryDtoList(categoryDto.getIdx(), mapList));
            result.add(categoryDto);
        }
        return result;
    }

    @Transactional
    public Integer register(CategoryDto categoryDto) {
        if (categoryRepository.findByNameAndRemovedFalse(categoryDto.getName()).isPresent())
            throw new AlreadyExistEntityException("이미 같은 이름의 카테고리가 있습니다");

        Category parent = getParentCategory(categoryDto.getPidx());   //부모 카테고리를 구한다, 없으면 null
        int nextCnt = countChildren(categoryDto.getPidx()) + 1;

        Category category = Category.builder()
                .name(categoryDto.getName())
                .parent(parent)
                .code(makeCode(parent, nextCnt))
                .priority(categoryDto.getPriority())
                .build();

        categoryRepository.save(category);
        return category.getIdx();
    }

    private String makeCode(Category parent, int cnt) {
        return (parent == null ? "" : parent.getCode()) + String.format("%02d", cnt);
    }

    private Category getParentCategory(Integer pid) {
        if (pid == null || pid == 0)
            return null;
        return categoryRepository.getByIdxAndAndRemovedFalse(pid);   //부모 카테고리를 구한다, 없으면 null;
    }

    private Integer countChildren(Integer pid) {
        if (pid == null || pid == 0)
            return categoryRepository.countPidIsNUll();
        return categoryRepository.countChildren(pid);
    }



    @Transactional
    public void modify(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getIdx())
                .orElseThrow(() -> new NotExistEntityException("해당 카테고리가 없습니다"));
        Category parent = getParentCategory(categoryDto.getPidx());
        int nextCnt = countChildren(categoryDto.getPidx()) + 1;

        category.changeParent(parent);
        category.changeName(categoryDto.getName());
        category.changeCode(makeCode(parent, nextCnt));
        category.changePriority(categoryDto.getPriority());
        category.changeName(categoryDto.getName());
    }

    /*하위 카테고리 모두 삭제*/
    @Transactional
    public void remove(Integer categoryIdx) {
        List<Category> categories = categoryRepository.findAllAssociatedWithIdx(categoryIdx);
        for(Category category : categories) {
            category.removed();
        }
    }
}

