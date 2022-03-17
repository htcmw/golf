package com.reborn.golf.review.entity;

import com.reborn.golf.customerservice.entity.Customerservice;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomerserviceReply extends Reply {

    @ManyToOne(fetch = FetchType.LAZY)
    private Customerservice customerservice;

    @Builder
    public CustomerserviceReply(Customerservice customerservice) {
        this.customerservice = customerservice;
    }
}
