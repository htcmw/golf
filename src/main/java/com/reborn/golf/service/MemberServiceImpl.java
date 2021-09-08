package com.reborn.golf.service;

import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean register(MemberDto memberDto){
        Optional<Member> result= memberRepository.findByEmail(memberDto.getEmail(),false);
        if (result.isEmpty()){
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
            Member newMember =  dtoToEntity(memberDto);
            memberRepository.save(newMember);
            return true;
        }
        return false;
    }

    @Override
    public MemberDto read(String email) {
        Optional<Member> result = memberRepository.findByEmail(email,false);
        return entityToDto(result.get());
    }

    //소셜로그인정보를 제외하고 모두 수정한다.
    @Override
    public void modify(MemberDto memberDto) {
        Optional<Member> result = memberRepository.findByEmail(memberDto.getEmail(),false);
        if(result.isPresent()){
            Member member = result.get();
            member.changePassword(memberDto.getPassword());
            member.changeName(memberDto.getName());
            member.changeAddress(memberDto.getAddress());
            member.changePhone(memberDto.getPhone());
            memberRepository.save(member);
        }
    }

    @Override
    public void remove(String email) {
        memberRepository.deleteByEmail(email);
    }
}
