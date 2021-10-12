package com.reborn.golf.service;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.kakaopay.KakaoPayResponseDto;
import com.reborn.golf.dto.shop.OrdersDto;
import com.reborn.golf.entity.*;
import com.reborn.golf.entity.Enum.DeliveryStatus;
import com.reborn.golf.entity.Enum.OrderStatus;
import com.reborn.golf.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;
    private final ChargeService chargeService;

    @Override
    @Transactional
    public PageResultDto<Orders, OrdersDto> getList(PageRequestDto pageRequestDto) {
        Page<Orders> result = orderRepository.getOrdersByRemovedFalse(pageRequestDto.getPageable(Sort.by("idx").descending()));
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    public PageResultDto<Orders, OrdersDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto) {
        Page<Orders> result = orderRepository.getOrdersByMemberIdxAndRemovedFalse(memberIdx, pageRequestDto.getPageable(Sort.by("idx").descending()));
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public String order(Integer memberIdx, OrdersDto ordersDto) {
        List<OrderProduct> orderProducts = orderProductService.makeOrderProduct(ordersDto.getOrderProductList());

        Member member = Member.builder().idx(memberIdx).build();

        Delivery delivery = Delivery.builder().address(ordersDto.getAddress()).deliveryStatus(DeliveryStatus.NULL).build();

        Integer totalPrice = orderProducts.stream().mapToInt(OrderProduct::getPrice).sum();

        String itemName = orderProducts.get(0).getProduct().getTitle() + " 외 " + (orderProducts.size() - 1);

        String partnerOrderId = LocalDate.now() + UUID.randomUUID().toString();

        Integer taxFreeAmount = 0;
        try {
            KakaoPayResponseDto kakaoPayResponseDto = chargeService.readyPayment(partnerOrderId, member.getEmail(), itemName, orderProducts.size(), totalPrice, taxFreeAmount);

            Orders orders = Orders.builder()
                    .orderState(OrderStatus.ORDER)
                    .member(member)
                    .delivery(delivery)
                    .orderProducts(orderProducts)
                    .partnerOrderId(partnerOrderId)
                    .tid(kakaoPayResponseDto.getTid())
                    .totalPrice(totalPrice)
                    .taxFreeAmount(taxFreeAmount)
                    .orderProductsCount(orderProducts.size())
                    .chargeCreatedAt(kakaoPayResponseDto.getCreated_at())
                    .build();

            orderRepository.save(orders);

            return kakaoPayResponseDto.getNext_redirect_pc_url();

        } catch (URISyntaxException | RestClientException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    @Transactional
    public Long cancel(Integer memberIdx, Long orderIdx) {

        Optional<Orders> optionalOrders = orderRepository.findById(orderIdx);

        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            try {
                chargeService.cancelPayment(orders);
                orders.changeIsRemoved(false);
                orderProductService.removeOrderProduct(orders.getOrderProducts());

                return orders.getIdx();

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
