package com.reborn.golf.entity;

import com.reborn.golf.dto.CartDto;
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

    private Integer quentity;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public void changeQuentity(Integer quentity){
        this.quentity = quentity;
    }


    public CartDto toCartDto(){
        return CartDto.builder()
                .cartIdx(getIdx())
                .quentity(getQuentity())
                .productIdx(getProduct().getIdx())
                .imageDtoList(getProduct().getProductImages().get(0).ProductImageDto())
                .title(getProduct().getTitle())
                .totalPrice(getQuentity() * getProduct().getPrice())
                .price(getProduct().getPrice())
                .modDate(getModDate())
                .regDate(getRegDate())
                .build();
    }

}
