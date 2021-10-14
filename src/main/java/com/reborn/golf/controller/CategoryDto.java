package com.reborn.golf.controller;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Integer idx;
    private String name;
    private String code;
    private Integer priority;
    private List<CategoryDto> categories;
}
