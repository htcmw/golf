package com.reborn.golf.test;

import com.reborn.golf.dto.shop.OrderProductDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public class Test {
    private String impUid;
    private String orderNumber;
    private String userId;
    private String email;
    private String buyerName;
    private String orderName;
    private String deliveryMessage;
    private String address;
    private Integer amount;
    private Integer useSavings;
    private List<OrderProductDto> cartIdList;
}
