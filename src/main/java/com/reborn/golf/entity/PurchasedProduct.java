package com.reborn.golf.entity;

import com.reborn.golf.entity.Enum.PurchasedProductStep;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Category catagory;

    private String name;

    private String brand;

    private String state;

    private Integer price;

    private Integer quentity;

    private String details;

    private String address;
    @Setter
    private Integer possiblePrice;
    @Setter
    private Integer acceptedPrice;
    @Setter
    private Long acceptedTokenAmount;

    private boolean canceled;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "purchasedProduct",  orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PurchasedProductImage> purchasedProductImages;

    @Enumerated(EnumType.STRING)
    private PurchasedProductStep purchasedProductStep;

    public void setStep(PurchasedProductStep purchasedProductStep){
        this.purchasedProductStep = purchasedProductStep;
    }

    public void changeCatagory(Integer catagoryIdx) {
        this.catagory = Category.builder().idx(catagoryIdx).build();
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

    public void changeAddress(String address) {
        this.address = address;
    }

    @PrePersist
    public void prePersist() {
        this.possiblePrice = (this.possiblePrice == null ? 0 : this.possiblePrice);
    }
}
