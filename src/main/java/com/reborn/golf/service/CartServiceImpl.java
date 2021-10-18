package com.reborn.golf.service;

import com.reborn.golf.dto.exception.AlreadyExistEntityException;
import com.reborn.golf.dto.exception.ImpossibleDeleteException;
import com.reborn.golf.dto.exception.NotExistEntityException;
import com.reborn.golf.dto.shop.CartDto;
import com.reborn.golf.dto.shop.CartListDto;
import com.reborn.golf.entity.Carts;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Product;
import com.reborn.golf.repository.CartRepository;
import com.reborn.golf.repository.MemberRepository;
import com.reborn.golf.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

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
        Product product = productRepository.getProductByIdx(cartDto.getProductIdx())
                .orElseThrow(() -> new NotExistEntityException("해당 상품이 없습니다"));
        Member member = memberRepository.getMemberByIdxAndRemovedFalse(memberIdx)
                .orElseThrow(() -> new NotExistEntityException("해당 유저가 없습니다"));

        Carts carts = Carts.builder()
                .member(member)
                .product(product)
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
