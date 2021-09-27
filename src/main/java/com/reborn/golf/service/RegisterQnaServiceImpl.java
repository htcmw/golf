package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.RegisteredQnaDto;
import com.reborn.golf.entity.RegisteredQna;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RegisterQnaServiceImpl implements RegisteredQnaService{
    @Override
    public PageResultDto<RegisteredQna, RegisteredQnaDto> getList(PageRequestDto pageRequestDto) {
        return null;
    }

    @Override
    public RegisteredQnaDto read(Long idx) {
        return null;
    }

    @Override
    public void modify(RegisteredQnaDto registeredQnaDto) {

    }

    @Override
    public void remove(Long idx) {

    }
}
