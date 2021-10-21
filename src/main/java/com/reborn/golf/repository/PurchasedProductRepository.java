package com.reborn.golf.repository;

import com.reborn.golf.entity.PurchasedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct,Long> {

    @Query(value =
                    "SELECT p, pi, c.name " +
                    "FROM PurchasedProduct p " +
                            "LEFT JOIN p.purchasedProductImages pi " +
                            "LEFT JOIN p.member m " +
                            "LEFT JOIN p.catagory c " +
                    "WHERE m.idx = :memberIdx " +
                            "AND p.canceled = false " +
                    "GROUP BY p ")
    Page<Object[]> getPurchasedItemsbyMemberIdx(Integer memberIdx, Pageable pageable);

    @Query(value =
                    "SELECT p " +
                    "FROM PurchasedProduct p " +
                            "LEFT JOIN p.purchasedProductImages pi " +
                            "LEFT JOIN p.member m " +
                    "WHERE m.idx = :memberIdx " +
                            "AND p.canceled = false " +
                            "AND p.idx = :idx")
    Optional<PurchasedProduct> getPurchasedItembyIdxAndMemberIdx(Integer memberIdx, Long idx);


    @Query(value =
            "SELECT p, pi, m " +
                    "FROM PurchasedProduct p " +
                            "LEFT JOIN p.purchasedProductImages pi " +
                            "LEFT JOIN p.member m " +
                    "WHERE m.idx = :memberIdx " +
                            "AND p.idx = :idx " +
                            "AND p.canceled = false " +
                    "GROUP BY pi")
    List<Object[]> getItembyIdxAndMemberIdxWithImage(Integer memberIdx, Long idx);



    Optional<PurchasedProduct> getPurchasedProductByIdxAndCanceledFalse(Long idx);
}
