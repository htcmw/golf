package com.reborn.golf.service;


import com.reborn.golf.dto.AssociatesDto;
import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.entity.Associates;
import com.reborn.golf.entity.Member;

public interface AssociatesService {
    boolean register(AssociatesDto associatesDto);

    AssociatesDto read(Integer email);

    Integer modify(Integer idx, AssociatesDto associatesDto);

    Integer remove(Integer idx);

    String searchEmail(AssociatesDto phone);

    Integer searchPassword(AssociatesDto associatesDto);

    default Associates dtoToEntity(AssociatesDto associatesDto) {
        return Associates.builder()
                .idx(associatesDto.getIdx())
                .email(associatesDto.getEmail())
                .password(associatesDto.getPassword())
                .phone(associatesDto.getPhone())
                .name(associatesDto.getName())
                .address(associatesDto.getAddress())
                .build();
    }

    default AssociatesDto entityToDto(Associates associates) {
        return AssociatesDto.builder()
                .idx(associates.getIdx())
                .email(associates.getEmail())
                .password(associates.getPassword())
                .phone(associates.getPhone())
                .name(associates.getName())
                .address(associates.getAddress())
                .build();
    }
}
