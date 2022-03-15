package com.reborn.golf.order.service;

import com.reborn.golf.order.dto.OrderProductDto;
import com.reborn.golf.order.entity.OrderProduct;
import com.reborn.golf.order.entity.Orders;

import java.util.HashMap;
import java.util.List;

public interface OrderProductService {
    HashMap<String, Object> makeOrderProduct(List<OrderProductDto> orderProductList, Orders orders);
    void removeOrderProduct(List<OrderProduct> orderProducts);
}
