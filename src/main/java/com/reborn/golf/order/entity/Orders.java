package com.reborn.golf.order.entity;

import com.reborn.golf.common.entity.BaseEntity;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.order.dto.OrderProductDto;
import com.reborn.golf.common.dto.ImageDto;
import com.reborn.golf.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"member", "orderState", "orderProducts", "delivery"})
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String impUid;  //아임포트 아이디

    @Column(nullable = false)
    private String orderNumber; //아임포트 주문 결제 idx

    @Column(nullable = false)
    private String orderName;   //주문 이름

    @Setter
    @Column(nullable = false)
    private Integer allProductPrice; //주문한 총 금액

    @Column(nullable = false)
    private Integer totalPrice; //주문한 총 금액

    @Column(nullable = false)
    private Double tokenPrice;  //포인트 가격

    @Column(nullable = false)
    private Long tokenAmount;   //포인트 수량

    @Setter
    @Column
    private long pointAmountToBuyer;//유저에게 지불한 포인트 수

    private boolean removed;    //취소 여부

    @Enumerated(EnumType.STRING)
    private OrderStatus orderState; //주문 상태

    @Column
    private Integer orderProductsCount; //상품 종류 수

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "orders")
    private List<OrderProduct> orderProducts;   //유저 정보

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;  //구매 물품 및 수량

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;  //배달 정보

    public void cancel() {
        this.removed = false;
        orderState = OrderStatus.CANCEL;
    }

    public List<OrderProductDto> toOrderProductDto() {
        List<OrderProductDto> orderProductDtos = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {

            Product product = orderProduct.getProduct();

            List<ImageDto> imageDtoList = product.getProductImages().stream().map(productImage ->
                    ImageDto.builder()
                            .imgName(productImage.getImgName())
                            .path(productImage.getPath())
                            .uuid(productImage.getUuid())
                            .build()
            ).collect(Collectors.toList());

            OrderProductDto orderProductDto = OrderProductDto.builder()
                    .orderProductIdx(orderProduct.getIdx())
                    .price(orderProduct.getPrice())
                    .quantity(orderProduct.getQuantity())
                    .productIdx(product.getIdx())
                    .title(product.getTitle())
                    .brand(product.getBrand())
                    .content(product.getContent())
                    .imageDtoList(imageDtoList)
                    .isRemoved(orderProduct.isRemoved())
                    .build();
            orderProductDtos.add(orderProductDto);
        }
        return orderProductDtos;
    }
}