package com.reborn.golf.category;

import com.reborn.golf.category.dto.CategoryDto;
import com.reborn.golf.category.entity.Category;
import com.reborn.golf.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CategoryRepositoryTests {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void initCategory() {
        String[] arr1 = {"BEST", "골프클럽", "골프용품", "골프웨어", "판매신청", "커뮤니티"};
        int cnt = 0;
        for (String temp : arr1) {
            cnt = cnt + 1;
            categoryRepository.save(Category.builder().name(temp).priority(cnt).code(String.format("%02d", cnt)).build());
        }

        String[][] arr = {{}, {"드라이버", "우드", "아이언", "풀세트"}, {"골프공", "골프가방", "골프장갑", "골프모자", "골프화"}, {"아우터", "상의", "하의", "양말"}, {}, {}};
        cnt = 0;
        for (int i = 0; i < arr.length; i++) {
            Category category = categoryRepository.getByIdxAndAndRemovedFalse(i + 1);
            for (String temp : arr[i]) {
                cnt = cnt + 1;
                Category category1 = Category.builder()
                        .code(category.getCode() + String.format("%02d", cnt))
                        .name(temp)
                        .priority(cnt)
                        .build();
                category1.setParent(category);
                categoryRepository.save(category1);
            }
        }
    }

    @Test
    public void getListTest() {
        List<CategoryDto> categories = categoryRepository.findAllByRemovedFalse(Sort.by("priority").ascending());
        Map<Integer, List<CategoryDto>> mapList = new HashMap<>();
        for (CategoryDto category : categories) { //grouping
            List<CategoryDto> categoryDtos = mapList.getOrDefault(category.getPidx(), new ArrayList<>());
            categoryDtos.add(category);
            mapList.put(category.getPidx(), categoryDtos);
        }
        List<CategoryDto> result = makeCategoryDtoList(null, mapList);

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

    @Test
    public void registerTest() {
        Integer pid = null;
        Category parent = categoryRepository.getByIdxAndAndRemovedFalse(pid);
        Category category = categoryRepository.findByNameAndRemovedFalse("골프클럽").get();

        assertThat(parent).isEqualTo(null);
        assertThat(category.getName()).isEqualTo("골프클럽");
    }

    @Test
    public void countOfChildrenTest() {
        Integer cnt = categoryRepository.countPidIsNUll();
        assertThat(cnt).isEqualTo(6);
        cnt = categoryRepository.countChildren(2);
        assertThat(cnt).isEqualTo(6);
    }

    @Test
    public void findAllAssociatedWithIdxTest() {
        List<Category> categories = categoryRepository.findAllAssociatedWithIdx(1);
        for (Category category : categories){
            System.out.println(category);
        }
    }
}
