package com.reborn.golf.service;

import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.MemberRole;
import com.reborn.golf.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class AssociatesServiceImpl implements MemberService {
    //비밀번호 암호화
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;


    @Override
    public boolean register(MemberDto memberDto) {
        Optional<Member> result = memberRepository.getMemberByEmailAndRemovedFalse(memberDto.getEmail());

        //추가 1. 삭제된 정보의 경우 언제부터 다시 회원가입 가능한지 조건 필요

        if (result.isEmpty()) {
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
            Member newMember = dtoToEntity(memberDto);
            newMember.addMemberAuthority(MemberRole.ROLE_USER);
            log.info(newMember);
            memberRepository.save(newMember);
            return true;
        }
        return false;
    }

    @Override
    public MemberDto read(Integer idx) {

        Optional<Member> result = memberRepository.getMemberByIdxAndRemovedFalse(idx);

        return result.map(this::entityToDto).orElse(null);

    }

    //이메일, 소셜 로그인 정보를 제외하고 모두 수정할 수 있다.
    @Override
    public void modify(Integer idx, MemberDto memberDto) {

        Optional<Member> result = memberRepository.getMemberByIdxAndRemovedFalse(idx);

        if (result.isPresent()) {

            Member member = result.get();

            if (member.getEmail().equals(memberDto.getEmail())) {

                member.changePassword(passwordEncoder.encode(memberDto.getPassword()));
                member.changeName(memberDto.getName());
                member.changeAddress(memberDto.getAddress());
                member.changePhone(memberDto.getPhone());

                log.info(member);
                memberRepository.save(member);

            }
        }
    }

    @Override
    @Transactional
    public void remove(Integer idx) {

        Optional<Member> result = memberRepository.getMemberByIdxAndRemovedFalse(idx);

        if (result.isPresent()) {

            Member member = result.get();
            member.changeIsRemoved(true);

            log.info(member);
            memberRepository.save(member);
        }
    }
}