package com.reborn.golf.service;


import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.MemberRole;

public interface MemberService {
    boolean register(MemberDto memberDto);

    MemberDto read(Integer email);

    void modify(Integer idx, MemberDto memberDto);

    void remove(Integer idx);

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
