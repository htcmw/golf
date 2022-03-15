package com.reborn.golf.product.entity;

import com.reborn.golf.common.entity.BaseEntity;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.product.dto.CartDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Carts extends BaseEntity {

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
                .imageDtoList(getProduct().getProductImages().get(0).toProductImageDto())
                .title(getProduct().getTitle())
                .totalPrice(getQuantity() * getProduct().getPrice())
                .price(getProduct().getPrice())
                .modDate(getModDate())
                .regDate(getRegDate())
                .build();
    }

}
