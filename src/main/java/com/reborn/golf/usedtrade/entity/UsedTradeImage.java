package com.reborn.golf.usedtrade.entity;

import com.reborn.golf.common.entity.Image;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@ToString(exclude = "usedTrade") //연관 관계시 항상 주의
public class UsedTradeImage extends Image {
    @ManyToOne(fetch = FetchType.LAZY)
    private UsedTrade usedTrade;

    @Builder
    public UsedTradeImage(Long idx, String uuid, String imgName, String path, boolean removed, UsedTrade usedTrade) {
        super(idx, uuid, imgName, path, removed);
        this.usedTrade = usedTrade;
    }
}
