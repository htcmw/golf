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
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    //회원가입시 비밀번호 암호화
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean register(MemberDto memberDto) {
        Optional<Member> result = memberRepository.findById(memberDto.getEmail());

        if (result.isEmpty()) {
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
            Member newMember = dtoToEntity(memberDto);
            log.info(newMember);
            memberRepository.save(newMember);
            return true;
        }
        return false;
    }

    @Override
    public MemberDto read(String email) {
        Optional<Member> result = memberRepository.findById(email);
        return result.map(this::entityToDto).orElse(null);

    }

    //이메일, 소셜 로그인 정보를 제외하고 모두 수정할 수 있다.
    @Override
    public void modify(MemberDto memberDto) {
        Optional<Member> result = memberRepository.findById(memberDto.getEmail());
        if (result.isPresent()) {
            Member member = result.get();
            member.changePassword(passwordEncoder.encode(memberDto.getPassword()));
            member.changeName(memberDto.getName());
            member.changeAddress(memberDto.getAddress());
            member.changePhone(memberDto.getPhone());
            log.info(member);
            memberRepository.save(member);
        }
    }

    @Override
    public void remove(String email) {
        memberRepository.deleteById(email);
    }
}
