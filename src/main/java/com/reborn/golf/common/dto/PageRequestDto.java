package com.reborn.golf.common.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class PageRequestDto {
    int page;
    int size;

    public PageRequestDto(){
        this.page = 1;
        this.size = 20;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1,size,sort);
    }
    public Pageable getPageable(){
        return PageRequest.of(page-1,size);
    }

}