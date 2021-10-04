package com.reborn.golf.service;

import com.reborn.golf.dto.AssociatesDto;
import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.entity.Associates;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.MemberRole;
import com.reborn.golf.repository.AssociatesRepository;
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
public class AssociatesServiceImpl implements AssociatesService {
    //비밀번호 암호화
    private final PasswordEncoder passwordEncoder;

    private final AssociatesRepository associatesRepository;


    @Override
    public boolean register(AssociatesDto associatesDto) {
        Optional<Associates> result = associatesRepository.getAssociatesByEmailAndRemovedFalse(associatesDto.getEmail());

        //추가 1. 삭제된 정보의 경우 언제부터 다시 회원가입 가능한지 조건 필요
        if (result.isEmpty()) {
            associatesDto.setPassword(passwordEncoder.encode(associatesDto.getPassword()));
            Associates newAssociates = dtoToEntity(associatesDto);
            newAssociates.addMemberAuthority(MemberRole.ROLE_USER);
            log.info(newAssociates);
            associatesRepository.save(newAssociates);
            return true;
        }
        return false;
    }

    @Override
    public AssociatesDto read(Integer idx) {

        Optional<Associates> result = associatesRepository.getAssociatesByIdxAndRemovedFalse(idx);

        return result.map(this::entityToDto).orElse(null);

    }

    //이메일, 소셜 로그인 정보를 제외하고 모두 수정할 수 있다.
    @Override
    public Integer modify(Integer idx, AssociatesDto associatesDto) {

        Optional<Associates> result = associatesRepository.getAssociatesByIdxAndRemovedFalse(idx);

        if (result.isPresent()) {

            Associates associates = result.get();

            if (associates.getEmail().equals(associatesDto.getEmail())) {
                associates.changeName(associatesDto.getName());
                associates.changeAddress(associatesDto.getAddress());
                associates.changePhone(associatesDto.getPhone());

                log.info(associates);
                associatesRepository.save(associates);
                return associates.getIdx();

            }
        }
        return null;
    }

    @Override
    @Transactional
    public Integer remove(Integer idx) {

        Optional<Associates> result = associatesRepository.getAssociatesByIdxAndRemovedFalse(idx);

        if (result.isPresent()) {

            Associates associates = result.get();
            associates.changeIsRemoved(true);

            log.info(associates);
            associatesRepository.save(associates);
            return associates.getIdx();
        }
        return null;
    }

    @Override
    public String searchEmail(AssociatesDto associatesDto) {
        Optional<Associates> result = associatesRepository.getAssociatesByEmailAndRemovedFalse(associatesDto.getEmail());
        if(result.isPresent()){
            Associates associates = result.get();
            return associates.getEmail();
        }
        return null;
    }

    @Override
    public Integer searchPassword(AssociatesDto associatesDto) {
        Optional<Associates> result = associatesRepository.getAssociatesByEmailAndPhoneAndRemovedFalse(associatesDto.getEmail(),associatesDto.getPhone());
        if(result.isPresent()){
            Associates associates = result.get();
            associates.changePhone(associatesDto.getPhone());
            return associates.getIdx();
        }
        return null;
    }

}
