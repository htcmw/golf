package com.reborn.golf.service;

import com.reborn.golf.dto.shop.CartDto;
import com.reborn.golf.dto.shop.CartListDto;

public interface CartService {
    CartListDto getList(Integer memberIdx);
    Long register(Integer memberIdx, CartDto cartDto);
    Long modify(Integer memberIdx, Long cartIdx ,Integer quentity);
    Long remove(Integer memberIdx, Long cartIdx);


}
