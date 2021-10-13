package com.reborn.golf.test;

import com.reborn.golf.dto.shop.OrderProductDto;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Test {
    private String impUid;
    private String orderNumber;
    private String userId;
    private String userEmail;
    private String userName;
    private String orderName;
    private String deliveryMessage;
    private String userAddress;
    private Integer totalPrice;
    private Integer tokenAmount;
    private List<OrderProductDto> orderProductList;
}
