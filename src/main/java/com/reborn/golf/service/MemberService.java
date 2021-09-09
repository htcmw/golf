package com.reborn.golf.service;


import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.MemberRole;

public interface MemberService {
    boolean register(MemberDto memberDto);

    MemberDto read(String email);

    void modify(MemberDto memberDto);

    void remove(String email);

    default Member dtoToEntity(MemberDto memberDto){
        System.out.println(memberDto);
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .phone(memberDto.getPhone())
                .name(memberDto.getName())
                .address(memberDto.getAddress())
                .fromSocial(memberDto.isFromSocial())
                .build();
        member.addMemberAuthority(MemberRole.USER);
        return member;
    }

    default MemberDto entityToDto(Member member){
        MemberDto memberDto = MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .phone(member.getPhone())
                .name(member.getName())
                .address(member.getAddress())
                .fromSocial(member.isFromSocial())
                .build();
        return memberDto;
    }
}
