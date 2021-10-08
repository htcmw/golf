package com.reborn.golf.repository;

import com.reborn.golf.entity.PurchasedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct,Long> {

    @Query(value =
                    "SELECT p " +
                    "FROM PurchasedProduct p " +
                            "LEFT JOIN p.purchasedProductImages pi " +
                            "LEFT JOIN p.member m " +
                    "WHERE m.idx = :memberIdx")
    Page<PurchasedProduct> getPurchasedItemsbyMemberIdx(Integer memberIdx, Pageable pageable);

    @Query(value =
                    "SELECT p " +
                    "FROM PurchasedProduct p " +
                            "LEFT JOIN p.purchasedProductImages pi " +
                            "LEFT JOIN p.member m " +
                    "WHERE m.idx = :memberIdx " +
                            "AND p.idx = :idx")
    Optional<PurchasedProduct> getPurchasedItembyIdxAndMemberIdx(Integer memberIdx, Long idx);

}
