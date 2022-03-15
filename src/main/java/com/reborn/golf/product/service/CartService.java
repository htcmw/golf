package com.reborn.golf.product.service;

import com.reborn.golf.product.dto.CartDto;
import com.reborn.golf.product.dto.CartListDto;

public interface CartService {
    CartListDto getList(Integer memberIdx);
    Long register(Integer memberIdx, CartDto cartDto);
    Long modify(Integer memberIdx, Long cartIdx ,Integer quentity);
    void remove(Integer memberIdx, Long cartIdx);
    void removeAll(Integer memberIdx);


}
