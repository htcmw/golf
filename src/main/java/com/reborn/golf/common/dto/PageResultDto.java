package com.reborn.golf.common.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class PageResultDto<Entity,Dto> {

    private List<Dto> dtoList;
    private int page;
    private int size;
    private int totalPages;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;
    List<Integer> pageNumberList;

    public PageResultDto(Page<Entity> entityPage, Function<Entity,Dto> function){
        dtoList = entityPage.map(function).stream().collect(Collectors.toList());
        totalPages = entityPage.getTotalPages();
        calculate(entityPage.getPageable());
    }

    private void calculate(Pageable pageable){
        page = pageable.getPageNumber() + 1;
        size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil(page / 1.0)) * 10;
        start = tempEnd - 9;
        end = Math.min(tempEnd, totalPages);
        prev = start > 1;
        next = tempEnd < totalPages;

        pageNumberList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }
}
