package com.reborn.golf.entity;

import com.reborn.golf.dto.OrderProductDto;
import com.reborn.golf.dto.ProductImageDto;
import com.reborn.golf.entity.Enum.OrderStatus;
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
@ToString (exclude = {"member","orderState","orderProducts","delivery"})
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue ( strategy =  GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private int totalPrice;
    // ORDER, CANCEL
    @Enumerated(EnumType.STRING)
    private OrderStatus orderState;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;

    @OneToMany (fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;

    @OneToOne (fetch = FetchType.LAZY)
    private Delivery delivery;

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
                    .rank(product.getRank())
                    .content(product.getContent())
                    .imageDtoList(productImageDtoList)
                    .build();
            orderProductDtos.add(orderProductDto);
        }
        return orderProductDtos;
    }
}