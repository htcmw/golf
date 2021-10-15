package com.reborn.golf.controller;

import com.reborn.golf.dto.shop.CategoryDto;
import com.reborn.golf.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Log4j2
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getList() {
        List<CategoryDto> categoryDtoList = categoryService.getList();
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> register(@RequestBody CategoryDto categoryDto) {
        log.info(categoryDto);
        return new ResponseEntity<>(categoryService.register(categoryDto), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Integer> modify(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.modify(categoryDto), HttpStatus.OK);

    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<Integer> remove(@PathVariable Integer idx) {
        categoryService.remove(idx);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
