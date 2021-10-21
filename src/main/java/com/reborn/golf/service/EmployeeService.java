package com.reborn.golf.service;


import com.reborn.golf.dto.user.MemberDto;
import com.reborn.golf.entity.Member;

public interface EmployeeService {
    void register(MemberDto employeeDto);

    MemberDto read(Integer email);

    Integer modify(Integer idx, MemberDto employeeDto);

    void remove(Integer idx);

    default Member dtoToEntity(MemberDto employeeDto) {
        return Member.builder()
                .idx(employeeDto.getIdx())
                .email(employeeDto.getEmail())
                .password(employeeDto.getPassword())
                .phone(employeeDto.getPhone())
                .name(employeeDto.getName())
                .address(employeeDto.getAddress())
                .build();
    }

    default MemberDto entityToDto(Member employee) {
        return MemberDto.builder()
                .idx(employee.getIdx())
                .email(employee.getEmail())
                .password(employee.getPassword())
                .phone(employee.getPhone())
                .name(employee.getName())
                .address(employee.getAddress())
                .build();
    }


    default MemberDto entityToDto(Member member, String walletAddress, Long tokenAmount){
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
}
