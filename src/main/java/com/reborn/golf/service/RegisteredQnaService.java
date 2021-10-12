package com.reborn.golf.service;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.customerservice.RegisteredQnaDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.RegisteredQna;

public interface RegisteredQnaService {

    PageResultDto<Object[], RegisteredQnaDto> getList(PageRequestDto pageRequestDto);

    RegisteredQnaDto read(Long idx);

    Long Register(Integer memberIdx, RegisteredQnaDto registeredQnaDto);

    Long modify(Integer memberIdx, RegisteredQnaDto registeredQnaDto);

    Long remove(Integer memberIdx, Long idx);

    default RegisteredQnaDto entityToDto(RegisteredQna registeredQna) {
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

    default RegisteredQnaDto entityToDto(RegisteredQna registeredQna, Member member) {
        return RegisteredQnaDto.builder()
                .idx(registeredQna.getIdx())
                .title(registeredQna.getTitle())
                .question(registeredQna.getQuestion())
                .answer(registeredQna.getAnswer())
                .email(member.getEmail())
                .name(member.getName())
                .views(registeredQna.getViews())
                .build();
    }

    default RegisteredQna dtoToEntity(RegisteredQnaDto registeredQnaDto) {
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
