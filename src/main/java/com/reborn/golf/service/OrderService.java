package com.reborn.golf.service;

import com.reborn.golf.dto.OrderDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.NoticeFractionation;

public interface OrderService {

    PageResultDto<Object[], OrderDto> getList(PageRequestDto pageRequestDto);
}
