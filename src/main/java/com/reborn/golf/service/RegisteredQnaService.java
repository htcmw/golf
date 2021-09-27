package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.RegisteredQnaDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.RegisteredQna;

public interface RegisteredQnaService {

    PageResultDto<RegisteredQna, RegisteredQnaDto> getList(PageRequestDto pageRequestDto);

    RegisteredQnaDto read(Long idx);

    void modify(RegisteredQnaDto registeredQnaDto);

    void remove(Long idx);

    default RegisteredQnaDto entityToDto(RegisteredQna registeredQna){
        return RegisteredQnaDto.builder()
                .idx(registeredQna.getIdx())
                .title(registeredQna.getTitle())
                .question(registeredQna.getQuestion())
                .answer(registeredQna.getAnswer())
                .email(registeredQna.getWriter().getEmail())
                .name(registeredQna.getWriter().getName())
                .views(registeredQna.getViews())
                .build();
    }

    default RegisteredQna dtoToEntity(RegisteredQnaDto registeredQnaDto){
        return RegisteredQna.builder()
                .idx(registeredQnaDto.getIdx())
                .title(registeredQnaDto.getTitle())
                .question(registeredQnaDto.getQuestion())
                .answer(registeredQnaDto.getAnswer())
                .writer(Member.builder().email(registeredQnaDto.getEmail()).name(registeredQnaDto.getName()).build())
                .views(registeredQnaDto.getViews())
                .build();
    }
}
