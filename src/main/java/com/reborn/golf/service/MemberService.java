package com.reborn.golf.service;


import com.reborn.golf.dto.user.MemberDto;
import com.reborn.golf.entity.Member;

public interface MemberService {
    void register(MemberDto memberDto);

    MemberDto read(Integer email);

    Integer modify(Integer idx, MemberDto memberDto);

    void remove(Integer idx);

    String searchEmail(MemberDto phone);

    Integer searchPassword(MemberDto memberDto);

    default Member dtoToEntity(MemberDto memberDto){
        return Member.builder()
                .idx(memberDto.getIdx())
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .phone(memberDto.getPhone())
                .name(memberDto.getName())
                .address(memberDto.getAddress())
                .fromSocial(memberDto.isFromSocial())
                .build();
    }

    default MemberDto entityToDto(Member member){
        return MemberDto.builder()
                .idx(member.getIdx())
                .email(member.getEmail())
                .password(member.getPassword())
                .phone(member.getPhone())
                .name(member.getName())
                .address(member.getAddress())
                .fromSocial(member.isFromSocial())
                .build();
    }
}
