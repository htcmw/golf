package com.reborn.golf.order.service;

import com.reborn.golf.common.exception.NotExistEntityException;
import com.reborn.golf.common.exception.ShortageOfStockException;
import com.reborn.golf.order.dto.OrderProductDto;
import com.reborn.golf.order.entity.OrderProduct;
import com.reborn.golf.order.entity.Orders;
import com.reborn.golf.order.repository.OrderProductRepository;
import com.reborn.golf.product.entity.Product;
import com.reborn.golf.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Log4j2
@RequiredArgsConstructor
public class OrderProductService {
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public List<OrderProduct> makeOrderProduct(List<OrderProductDto> orderProductList, Orders orders) {
        int totalPrice = 0;
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDto orderProductDto : orderProductList) {
            Product product = productRepository.getProductByIdx(orderProductDto.getProductIdx())
                    .orElseThrow(() -> new NotExistEntityException("취소한 물품이 DB에 없음"));

            product.changeSalesVolume(product.getSalesVolume() + orderProductDto.getQuantity());
            product.changeQuantity(product.getQuantity() - orderProductDto.getQuantity());
            if (product.getQuantity() < 0) throw new ShortageOfStockException("제고물량 부족");

            int orderProductPrice = product.getPrice() * orderProductDto.getQuantity();
            totalPrice += orderProductPrice;
            OrderProduct orderProduct = OrderProduct.builder()
                    .price(orderProductPrice)
                    .quantity(orderProductDto.getQuantity())
                    .product(product)
                    .orders(orders)
                    .build();

            orderProductRepository.save(orderProduct);
            orderProducts.add(orderProduct);
        }
        orders.setAllProductPrice(totalPrice);
        return orderProducts;
    }

    public void removeOrderProduct(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.removed();
            Product product = productRepository.getProductByIdx(orderProduct.getProduct().getIdx())
                    .orElseThrow(() -> new NotExistEntityException("취소한 물품이 DB에 없음"));
            product.changeQuantity(product.getQuantity() + orderProduct.getQuantity());
            product.changeSalesVolume(product.getSalesVolume() - orderProduct.getQuantity());
        }
    }
}
