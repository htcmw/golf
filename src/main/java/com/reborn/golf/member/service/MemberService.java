package com.reborn.golf.member.service;

import com.reborn.golf.member.dto.MemberDto;
import com.reborn.golf.member.dto.UserType;
import com.reborn.golf.member.entity.Member;

public interface MemberService {

    void register(MemberDto memberDto, UserType userType);

    MemberDto read(Integer email);

    Integer modify(Integer idx, MemberDto memberDto);

    void remove(Integer idx);

    String searchEmail(MemberDto phone);

    Integer searchPassword(MemberDto memberDto);

    default Member dtoToEntity(MemberDto memberDto) {
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

    default MemberDto entityToDto(Member member, String walletAddress, Long tokenAmount) {
        return MemberDto.builder()
                .idx(member.getIdx())
                .email(member.getEmail())
                .walletAddress(walletAddress)
                .tokenAmount(tokenAmount)
                .password(member.getPassword())
                .phone(member.getPhone())
                .name(member.getName())
                .address(member.getAddress())
                .fromSocial(member.isFromSocial())
                .build();
    }

    default MemberDto entityToDto(Member member) {
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
