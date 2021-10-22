package com.reborn.golf.service;

import com.reborn.golf.dto.exception.ShortageOfStockException;
import com.reborn.golf.dto.shop.OrderProductDto;
import com.reborn.golf.entity.OrderProduct;
import com.reborn.golf.entity.Orders;
import com.reborn.golf.entity.Product;
import com.reborn.golf.repository.OrderProductRepository;
import com.reborn.golf.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public HashMap<String, Object> makeOrderProduct(List<OrderProductDto> orderProductList, Orders orders) {

        List<OrderProduct> orderProducts = new ArrayList<>();
        int totalPrice = 0;
        log.info(orderProductList);
        for (OrderProductDto orderProductDto : orderProductList) {

            Optional<Product> optionalProduct = productRepository.getProductByIdx(orderProductDto.getProductIdx());
            if (optionalProduct.isPresent()) {

                Product product = optionalProduct.get();
                int quantity = product.getQuantity() - orderProductDto.getQuantity();
                if (quantity < 0) {
                    throw new ShortageOfStockException("제고물량 부족");
                }
                product.changeQuantity(quantity);
                product.changeSalesVolume(product.getSalesVolume() + orderProductDto.getQuantity());
                productRepository.save(product);


                int price = product.getPrice() * orderProductDto.getQuantity();
                OrderProduct orderProduct = OrderProduct.builder()
                        .price(price)
                        .quantity(orderProductDto.getQuantity())
                        .product(product)
                        .orders(orders)
                        .build();
                totalPrice += price;
                orderProductRepository.save(orderProduct);
                orderProducts.add(orderProduct);
            }
        }
        log.info(orderProducts);

        HashMap<String, Object> map = new HashMap<>();
        map.put("orderProducts", orderProducts);
        map.put("totalPrice", totalPrice);

        return map;
    }

    public void removeOrderProduct(List<OrderProduct> orderProducts) {

        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.changeIsRemoved(true);

            Optional<Product> optionalProduct = productRepository.getProductByIdx(orderProduct.getIdx());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.changeQuantity(product.getQuantity() + orderProduct.getQuantity());
                product.changeSalesVolume(product.getSalesVolume() - orderProduct.getQuantity());
                productRepository.save(product);
            }
        }
    }
}
