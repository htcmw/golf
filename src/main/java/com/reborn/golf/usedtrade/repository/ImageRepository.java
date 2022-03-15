package com.reborn.golf.usedtrade.repository;

import com.reborn.golf.usedtrade.entity.UsedTradeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ImageRepository extends JpaRepository<UsedTradeImage, Long> {
    @Modifying
    void deleteAllByUsedTradeIdx(Long usedTradeIdx);
}
