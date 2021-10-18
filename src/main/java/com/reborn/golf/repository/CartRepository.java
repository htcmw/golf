package com.reborn.golf.repository;

import com.reborn.golf.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Carts, Long> {
    List<Carts> getCartsByMemberIdx(Integer memberIdx);
    Optional<Carts> getCartsByIdxAndMemberIdx(Long cartIdx, Integer memberIdx);


    //장바구니 비우기
    @Modifying
    @Query("DELETE FROM Carts c WHERE c in :carts")
    void deleteCartsByMemberIdx(List<Carts> carts);
}
