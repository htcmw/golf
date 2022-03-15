package com.reborn.golf.usedtrade.repository;

import com.reborn.golf.usedtrade.entity.UsedTrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsedTradeRepository extends JpaRepository<UsedTrade,Long> {
    @Query(value =
                    "SELECT p, " +
                            "pi, " +
                            "c.name " +
                    "FROM UsedTrade p " +
                            "LEFT JOIN p.usedTradeImages pi " +
                            "LEFT JOIN p.member m " +
                            "LEFT JOIN p.catagory c " +
                    "WHERE m.idx = :memberIdx " +
                    "GROUP BY p ")
    Page<Object[]> getPurchasedItemsbyMemberIdx(Integer memberIdx, Pageable pageable);


    @Query(value =
                    "SELECT p, " +
                            "pi, " +
                            "m, " +
                            "c.name " +
                    "FROM UsedTrade p " +
                            "LEFT JOIN p.usedTradeImages pi " +
                            "LEFT JOIN p.member m " +
                            "LEFT JOIN p.catagory c " +
                    "GROUP BY p ")
    Page<Object[]> getPurchasedProductByIdx(Pageable pageable);


    @Query(value =
                    "SELECT p " +
                    "FROM UsedTrade p " +
                            "LEFT JOIN p.usedTradeImages pi " +
                            "LEFT JOIN p.member m " +
                    "WHERE m.idx = :memberIdx " +
                            "AND p.canceled = false " +
                            "AND p.idx = :idx")
    Optional<UsedTrade> getPurchasedItembyIdxAndMemberIdx(Integer memberIdx, Long idx);


    @Query(value =
                    "SELECT p, " +
                            "pi, " +
                            "m " +
                    "FROM UsedTrade p " +
                            "LEFT JOIN p.usedTradeImages pi " +
                            "LEFT JOIN p.member m " +
                    "WHERE p.idx = :idx " +
                    "GROUP BY pi")
    List<Object[]> getItembyIdxWithImage(Long idx);



    Optional<UsedTrade> getPurchasedProductByIdxAndCanceledFalse(Long idx);
}
