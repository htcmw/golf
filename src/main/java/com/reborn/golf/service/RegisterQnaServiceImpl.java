package com.reborn.golf.service;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.customerservice.RegisteredQnaDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.RegisteredQna;
import com.reborn.golf.repository.RegisteredQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegisterQnaServiceImpl implements RegisteredQnaService{

    private final RegisteredQnaRepository registeredQnaRepository;

    @Override
    public Long Register(Integer memberIdx, RegisteredQnaDto registeredQnaDto) {
        RegisteredQna registeredQna = dtoToEntity(registeredQnaDto);
        registeredQnaRepository.save(registeredQna);
        return registeredQna.getIdx();
    }

    @Override
    public PageResultDto<Object[], RegisteredQnaDto> getList(PageRequestDto pageRequestDto) {
        Page<Object[]> result = registeredQnaRepository.getRegisteredQnasWithMember(pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        Function<Object[], RegisteredQnaDto> function = (entity -> entityToDto((RegisteredQna) entity[0], (Member) entity[1]));
        return new PageResultDto<>(result,function);
    }

    @Override
    public RegisteredQnaDto read(Long idx) {
        Optional<RegisteredQna> result = registeredQnaRepository.getRegisteredQnaByIdx(idx);
        if(result.isPresent()){
            RegisteredQna registeredQna = result.get();
            registeredQna.addViews();
            registeredQnaRepository.save(registeredQna);
            return entityToDto(registeredQna);
        }
        return null;
    }

    @Override
    public Long modify(Integer memberIdx, RegisteredQnaDto registeredQnaDto) {
        Optional<RegisteredQna> result = registeredQnaRepository.getRegisteredQnaByIdx(registeredQnaDto.getIdx());

        if(result.isPresent()){
            RegisteredQna registeredQna = result.get();
            registeredQna.chageWriter(memberIdx);
            registeredQna.changeTitle(registeredQnaDto.getTitle());
            registeredQna.changeQuestion(registeredQnaDto.getQuestion());
            registeredQna.changeAnswer(registeredQna.getAnswer());
            return registeredQna.getIdx();
        }
        return null;
    }

    @Override
    public Long remove(Integer memberIdx, Long idx) {
        Optional<RegisteredQna> result = registeredQnaRepository.getRegisteredQnaByIdx(idx);

        if(result.isPresent()){
            RegisteredQna registeredQna = result.get();
            registeredQna.chageWriter(memberIdx);
            registeredQna.changeRemoved(true);
            registeredQnaRepository.save(registeredQna);
            return registeredQna.getIdx();
        }
        return null;
    }
}
