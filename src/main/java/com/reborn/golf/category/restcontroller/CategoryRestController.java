package com.reborn.golf.category.restcontroller;

import com.reborn.golf.category.dto.CategoryDto;
import com.reborn.golf.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryRestController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getList() {
        return ResponseEntity.ok(categoryService.getList());
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Integer> register(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.register(categoryDto));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<Integer> modify(@RequestBody CategoryDto categoryDto) {
        categoryService.modify(categoryDto);
        return ResponseEntity.ok(categoryDto.getIdx());
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{idx}")
    public ResponseEntity<Integer> remove(@PathVariable Integer idx) {
        categoryService.remove(idx);
        return ResponseEntity.ok(idx);
    }


}
