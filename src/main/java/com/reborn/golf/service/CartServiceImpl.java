package com.reborn.golf.service;

import com.reborn.golf.dto.shop.CartDto;
import com.reborn.golf.dto.shop.CartListDto;
import com.reborn.golf.entity.Carts;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Product;
import com.reborn.golf.repository.CartRepository;
import com.reborn.golf.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Override
    public CartListDto getList(Integer memberIdx) {
        List<Carts> carts = cartRepository.getCartsByMemberIdx(memberIdx);

        List<CartDto> cartDtos = new ArrayList<>();
        int sum = 0;
        for(Carts cart : carts){
            CartDto cartDto = cart.toCartDto();
            sum += cartDto.getTotalPrice();
            cartDtos.add(cartDto);
        }

        return CartListDto.builder()
                .totalPrice(sum)
                .cartDtos(cartDtos)
                .build();
    }

    public Long register(Integer memberIdx, CartDto cartDto) {
        Optional<Product> optionalProduct = productRepository.getProductByIdx(cartDto.getProductIdx());

        if (optionalProduct.isPresent()) {
            Carts carts = Carts.builder()
                    .member(Member.builder().idx(memberIdx).build())
//                    .product(Product.builder().idx(cartDto.getProductIdx()).build())
                    .quantity(cartDto.getQuantity())
                    .build();
            cartRepository.save(carts);
            return carts.getIdx();
        }
        return null;
    }

    @Override
    public Long modify(Integer memberIdx, Long cartIdx, Integer quentity) {
        Optional<Carts> optionalCart = cartRepository.findById(cartIdx);
        if (optionalCart.isPresent()) {
            Carts carts = optionalCart.get();
            carts.changeQuentity(quentity);
            cartRepository.save(carts);
            return carts.getIdx();
        }
        return null;
    }

    @Override
    public Long remove(Integer memberIdx, Long cartIdx) {
        cartRepository.deleteById(cartIdx);
        return cartIdx;
    }
}
