package com.reborn.golf.entity;

import com.reborn.golf.dto.shop.OrderProductDto;
import com.reborn.golf.dto.shop.ProductImageDto;
import com.reborn.golf.entity.Enum.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString (exclude = {"member","orderState","orderProducts","delivery"})
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue ( strategy =  GenerationType.IDENTITY)
    private Long idx;
    //아임포트 아이디
//    @Column(nullable = false)
    private String impUid;
    //아임포트 주문 결제 idx
//    @Column(nullable = false)
    private String orderNumber;
    //주문 이름
//    @Column(nullable = false)
    private String orderName;
    //주문한 총 금액
//    @Column(nullable = false)
    private Integer totalPrice;
    //포인트
//    @Column(nullable = false)
    private Integer tokenAmount;
    //상품 종류 수
    @Column
    private Integer orderProductsCount;
    //주문 상태
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus orderState;
    //구매자
    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;
    //구매 물품 및 수량
    @OneToMany (fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "orders")
    private List<OrderProduct> orderProducts;
    //배달
    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;
    //취소 T or F
    private boolean removed;

    public void changeIsRemoved(boolean removed){
        this.removed = removed;
    }

    public List<OrderProductDto> toOrderProductDto(){
        List<OrderProductDto> orderProductDtos = new ArrayList<>();
        for(OrderProduct orderProduct : orderProducts){

            Product product = orderProduct.getProduct();

            List<ProductImageDto> productImageDtoList = product.getProductImages().stream().map(productImage ->
                            ProductImageDto.builder()
                                    .imgName(productImage.getImgName())
                                    .path(productImage.getPath())
                                    .uuid(productImage.getUuid())
                                    .build()
                    ).collect(Collectors.toList());

            OrderProductDto orderProductDto = OrderProductDto.builder()
                    .orderProductIdx(orderProduct.getIdx())
                    .price(orderProduct.getPrice())
                    .quentity(orderProduct.getQuantity())
                    .productIdx(product.getIdx())
                    .title(product.getTitle())
                    .brand(product.getBrand())
                    .content(product.getContent())
                    .imageDtoList(productImageDtoList)
                    .isRemoved(orderProduct.isRemoved())
                    .build();
            orderProductDtos.add(orderProductDto);
        }
        return orderProductDtos;
    }
}