package com.reborn.golf.category.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString(exclude = {"categories"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Integer idx;
    private Integer pidx;
    private String name;
    private String code;
    private Integer priority;
    private List<CategoryDto> categories;

    public CategoryDto(Integer idx, Integer pidx, String name, String code, Integer priority) {
        this.idx = idx;
        this.pidx = pidx;
        this.name = name;
        this.code = code;
        this.priority = priority;
    }
}
