package com.reborn.golf.service;

import com.reborn.golf.dto.exception.AlreadyExistEntityException;
import com.reborn.golf.dto.exception.DifferentEmailException;
import com.reborn.golf.dto.exception.NotExistEntityException;
import com.reborn.golf.dto.user.MemberDto;
import com.reborn.golf.entity.Enum.Role;
import com.reborn.golf.entity.Member;
import com.reborn.golf.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/*
 * 직원 CRUD 클래스
 * 직원 테이블을 따로 만들게 될경우 변경해야함
 * */
@Service
@Log4j2
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    //비밀번호 암호화
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    @Override
    public void register(MemberDto memberDto) {

        Optional<Member> result = memberRepository.getMemberByEmailAndRemovedFalse(memberDto.getEmail());

        if (result.isEmpty()) {
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
            Member newMember = dtoToEntity(memberDto);
            newMember.addMemberAuthority(Role.ROLE_MANAGER);
            log.info(newMember);
            memberRepository.save(newMember);
        } else {
            throw new AlreadyExistEntityException("같은 이메일이 이미 있습니다.");
        }
    }

    @Override
    public MemberDto read(Integer idx) {

        Member member = memberRepository.getMemberByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("NotExistEntityException"));
        ;

        return entityToDto(member);

    }

    @Override
    public Integer modify(Integer idx, MemberDto memberDto) {
        Member member = memberRepository.getMemberByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("NotExistEntityException"));

        if (member.getEmail().equals(memberDto.getEmail())) {
            member.changeName(memberDto.getName());
            member.changeAddress(memberDto.getAddress());
            member.changePhone(memberDto.getPhone());

            log.info(member);
            memberRepository.save(member);
            return member.getIdx();
        }
        throw new DifferentEmailException("이메일이 다릅니다");

    }

    @Override
    @Transactional
    public void remove(Integer idx) {

        Member member = memberRepository.getMemberByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("NotExistEntityException"));
        member.changeIsRemoved(true);
        log.info(member);
        memberRepository.save(member);
    }

}
