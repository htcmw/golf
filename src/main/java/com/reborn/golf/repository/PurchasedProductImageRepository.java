package com.reborn.golf.repository;

import com.reborn.golf.entity.PurchasedProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface PurchasedProductImageRepository extends JpaRepository<PurchasedProductImage,Long> {
    @Modifying
    void deleteAllByPurchasedProductIdx(Long PurchasedProductIdx);
}
