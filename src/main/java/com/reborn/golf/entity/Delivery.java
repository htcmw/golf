package com.reborn.golf.entity;

import com.reborn.golf.entity.Enum.DeliveryStatus;
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

    @Column(nullable = false)
    private String address;

    private String deliveryMessage;

    @Setter
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

}
