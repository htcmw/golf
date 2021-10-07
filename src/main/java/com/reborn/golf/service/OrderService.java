package com.reborn.golf.service;

import com.reborn.golf.dto.OrderDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.NoticeFractionation;
import com.reborn.golf.security.dto.AuthMemeberDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface OrderService {

    PageResultDto<Object[], OrderDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto);

    PageResultDto<Object[], OrderDto> getList(PageRequestDto pageRequestDto);

    Long order(Integer memberIdx, OrderDto orderDto);
    Long cancel(Integer memberIdx, Long orderIdx);

}
