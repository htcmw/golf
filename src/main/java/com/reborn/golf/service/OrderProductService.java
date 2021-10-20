package com.reborn.golf.service;

import com.reborn.golf.dto.shop.OrderProductDto;
import com.reborn.golf.entity.OrderProduct;
import com.reborn.golf.entity.Orders;

import java.util.HashMap;
import java.util.List;

public interface OrderProductService {
    HashMap<String, Object> makeOrderProduct(List<OrderProductDto> orderProductList, Orders orders);
    void removeOrderProduct(List<OrderProduct> orderProducts);
}
