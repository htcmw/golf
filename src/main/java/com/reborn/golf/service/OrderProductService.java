package com.reborn.golf.service;

import com.reborn.golf.dto.shop.OrderProductDto;
import com.reborn.golf.entity.OrderProduct;

import java.util.List;

public interface OrderProductService {
    List<OrderProduct> makeOrderProduct(List<OrderProductDto> orderProductList);
    void removeOrderProduct(List<OrderProduct> orderProducts);
}
