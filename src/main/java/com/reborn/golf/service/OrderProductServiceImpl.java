package com.reborn.golf.service;

import com.reborn.golf.dto.shop.OrderProductDto;
import com.reborn.golf.entity.OrderProduct;
import com.reborn.golf.entity.Product;
import com.reborn.golf.repository.OrderProductRepository;
import com.reborn.golf.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService{

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public List<OrderProduct> makeOrderProduct(List<OrderProductDto> orderProductList) {

        List<OrderProduct> orderProducts = new ArrayList<>();

        log.info(orderProductList);
        for (OrderProductDto orderProductDto : orderProductList) {

            Optional<Product> optionalProduct = productRepository.getProductByIdx(orderProductDto.getProductIdx());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.changeQuantity(product.getQuantity() - orderProductDto.getQuentity());
                productRepository.save(product);

                OrderProduct orderProduct = OrderProduct.builder()
                        .price(product.getPrice() * orderProductDto.getQuentity())
                        .quantity(orderProductDto.getQuentity())
                        .product(product)
                        .build();

                orderProductRepository.save(orderProduct);
                orderProducts.add(orderProduct);
            }
        }
        return orderProducts;
    }

    public void removeOrderProduct(List<OrderProduct> orderProducts){

        for(OrderProduct orderProduct : orderProducts){
            orderProduct.changeIsRemoved(true);

            Optional<Product> optionalProduct = productRepository.getProductByIdx(orderProduct.getIdx());
            if(optionalProduct.isPresent()){
                Product product = optionalProduct.get();
                product.changeQuantity(product.getQuantity() + orderProduct.getQuantity());
                productRepository.save(product);
            }
        }
    }
}
