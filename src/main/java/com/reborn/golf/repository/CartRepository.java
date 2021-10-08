package com.reborn.golf.repository;

import com.reborn.golf.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CartRepository extends JpaRepository<Carts, Long> {
    List<Carts> getCartsByMemberIdx(Integer memberIdx);
}
