package com.reborn.golf.product.service;

import com.reborn.golf.common.exception.ImpossibleDeleteException;
import com.reborn.golf.common.exception.NotExistEntityException;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.product.dto.CartDto;
import com.reborn.golf.product.dto.CartListDto;
import com.reborn.golf.product.entity.Carts;
import com.reborn.golf.product.entity.Product;
import com.reborn.golf.product.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public CartListDto getList(Integer memberIdx) {
        log.info(memberIdx);
        List<Carts> carts = cartRepository.getCartsByMemberIdx(memberIdx);

        List<CartDto> cartDtos = new ArrayList<>();
        int sum = 0;
        for (Carts cart : carts) {
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
            Carts carts = Carts.builder()
                    .member(Member.builder().idx(memberIdx).build())
                    .product(Product.builder().idx(cartDto.getProductIdx()).build())
                    .quantity(cartDto.getQuantity())
                    .build();

            cartRepository.save(carts);

            return carts.getIdx();
        }

        @Override
        public Long modify(Integer memberIdx, Long cartIdx, Integer quentity) {
            Carts carts = cartRepository.getCartsByIdxAndMemberIdx(cartIdx, memberIdx)
                    .orElseThrow(() -> new NotExistEntityException("해당하는 장바구니 정보가 없습니다"));

        carts.changeQuentity(quentity);
        cartRepository.save(carts);
        
        return carts.getIdx();
    }

    @Override
    public void remove(Integer memberIdx, Long cartIdx) {
        try{
            cartRepository.deleteById(cartIdx);
            log.info("삭제 성공");
        }catch (DataIntegrityViolationException e){
            throw new ImpossibleDeleteException("DB 정보 삭제 실패 : " + e.getMessage());
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("DB 정보 삭제 실패 : " + e.getMessage(), e.getExpectedSize());
        }

    }

    @Override
    @Transactional
    public void removeAll(Integer memberIdx) {
        try{
            List<Carts> carts = cartRepository.getCartsByMemberIdx(memberIdx);
            cartRepository.deleteCartsByMemberIdx(carts);
            log.info("삭제 성공");
        }catch (DataIntegrityViolationException e){
            throw new ImpossibleDeleteException("DB 정보 삭제 실패 : " + e.getMessage());
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("DB 정보 삭제 실패 : " + e.getMessage(), e.getExpectedSize());
        }
    }
}
