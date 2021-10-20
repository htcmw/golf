package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchasedProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String catagory;

    private String name;

    private String brand;

    private String state;

    private int price;

    private int quentity;

    private String details;

    private String memberAddress;

    private boolean finished;

    private boolean canceled;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "purchasedProduct",  orphanRemoval = true, cascade = CascadeType.ALL)
    List<PurchasedProductImage> purchasedProductImages;

    public void changeCatagory(String catagory) {
        this.catagory = catagory;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeBrand(String brand) {
        this.brand = brand;
    }

    public void changeState(String state) {
        this.state = state;
    }

    public void changeQuentity(int quentity) {
        this.quentity = quentity;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeDetails(String details) {
        this.details = details;
    }

    public void changeRemoved(Boolean canceled) {
        this.canceled = canceled;
    }

    public void changeAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public void changeFinished(Boolean finished) {
        this.finished = finished;
    }
}
