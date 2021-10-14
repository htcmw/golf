package com.reborn.golf.entity;

import com.reborn.golf.dto.shop.CartDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Carts extends BaseEntity{

    @Id
    @GeneratedValue( strategy =  GenerationType.IDENTITY)
    private Long idx;

    private Integer quantity;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public void changeQuentity(Integer quantity){
        this.quantity = quantity;
    }

    public CartDto toCartDto(){
        return CartDto.builder()
                .cartIdx(getIdx())
                .quantity(getQuantity())
                .productIdx(getProduct().getIdx())
                .imageDtoList(getProduct().getProductImages().get(0).ProductImageDto())
                .title(getProduct().getTitle())
                .totalPrice(getQuantity() * getProduct().getPrice())
                .price(getProduct().getPrice())
                .modDate(getModDate())
                .regDate(getRegDate())
                .build();
    }

}
