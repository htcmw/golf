package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "purchasedItems")
public class PurchasedItemsImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private PurchasedItems purchasedItems;
}
