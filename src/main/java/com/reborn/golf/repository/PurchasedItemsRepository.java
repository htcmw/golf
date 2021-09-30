package com.reborn.golf.repository;

import com.reborn.golf.entity.Product;
import com.reborn.golf.entity.PurchasedItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchasedItemsRepository extends JpaRepository<PurchasedItems,Long> {

    @Query(value =
            "SELECT p, m " +
            "FROM PurchasedItems p " +
                    "LEFT JOIN p.purchasedItemsImages pi " +
                    "LEFT JOIN p.member m " +
            "WHERE m.idx = :memberIdx")
    Page<Object[]> getPurchasedItemsbyMemberIdx(Integer memberIdx, Pageable pageable);



}
