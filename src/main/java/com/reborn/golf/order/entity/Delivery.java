package com.reborn.golf.order.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"deliveryStatus"})
public class Delivery {

    @Id
    @GeneratedValue( strategy =  GenerationType.IDENTITY)
    private Long idx;
    //받는 사람
    private String recipient;
    //배달 주소
    @Column(nullable = false)
    private String recipientAddress;
    //받는 사람 전화번호
    private String recipientPhone;
    //배달 메세지
    private String deliveryMessage;
    //배달 상태
    @Setter
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

}
