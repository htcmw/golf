package com.reborn.golf.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table (name = "product")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue
    private Long Id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String rank;

    @Column(nullable = false)
    private int quentity;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String origFilename;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;

    @Builder
    public Product(Long Id, String title, String brand, String rank, int quentity, int price,
                   String content, String origFilename, String filename, String filePath) {
        this.Id = Id;
        this.title = title;
        this.brand = brand;
        this.rank = rank;
        this.quentity = quentity;
        this.price = price;
        this.content = content;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}