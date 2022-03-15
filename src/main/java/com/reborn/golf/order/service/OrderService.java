package com.reborn.golf.order.service;

import com.reborn.golf.common.api.CoinExchange;
import com.reborn.golf.common.api.ContractService;
import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.common.exception.NotExistEntityException;
import com.reborn.golf.common.exception.PaymentException;
import com.reborn.golf.common.exception.TokenTransactionException;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.member.entity.Wallet;
import com.reborn.golf.order.api.IamportManager;
import com.reborn.golf.order.dto.OrderProductDto;
import com.reborn.golf.order.dto.OrdersDto;
import com.reborn.golf.order.entity.*;
import com.reborn.golf.order.repository.OrdersRepository;
import com.reborn.golf.product.dto.ProductImageDto;
import com.reborn.golf.product.entity.Product;
import com.reborn.golf.product.entity.ProductImage;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepository;
    //아임포트
    private final IamportManager iamportManager;
    //거래소의 코인 가격
    private final CoinExchange coinExchange;
    //클레이튼
    private final ContractService contractService;
    //OrderProduct
    private final OrderProductService orderProductService;

    @Transactional
    public OrdersDto runPayment(Integer memberIdx, OrdersDto ordersDto) {
        try {
            verificatePayment(ordersDto);
            Member member = ordersRepository.searchMemberWithWallet(memberIdx, ordersDto.getUserEmail(), ordersDto.getUserName())
                    .orElseThrow(() -> new IllegalAccessException("User information does not match"));
            OrdersDto order = order(memberIdx, ordersDto);
            order.setPointAmountToBuyer(transferPoint(ordersDto, member.getWallet()));
            return order;
        } catch (Exception e) {
            try {
                iamportManager.cancelPayment(ordersDto.getImpUid(), ordersDto.getTotalPrice(), false, "주문 중 에러: " + e.getMessage());
            } catch (IamportResponseException | IOException ex) {
                log.error("주문 취소 실패" + ex.getMessage());
            }
            throw new PaymentException(e.getMessage());
        }
    }

    private void verificatePayment(OrdersDto ordersDto) throws IllegalAccessException, IamportResponseException, IOException {
        if (!iamportManager.verificatePayment(ordersDto))
            throw new IllegalAccessException("결제 검증 실패");
    }

    private OrdersDto order(Integer memberIdx, OrdersDto ordersDto) throws IllegalAccessException {
        Member member = ordersRepository.searchMemberWithWallet(memberIdx, ordersDto.getUserEmail(), ordersDto.getUserName())
                .orElseThrow(() -> new IllegalAccessException("User information does not match"));
        Wallet wallet = member.getWallet();
        Delivery delivery = getDeliveryInfo(ordersDto);
        Orders orders = getOrderInfo(ordersDto, member, delivery);
        ordersRepository.save(orders);

        return makeOrderDto(orders, orderProductService.makeOrderProduct(ordersDto.getOrderProductList(), orders));
    }

    private Long transferPoint(OrdersDto orderDto, Wallet wallet) throws IOException, IllegalAccessException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        //토큰이 수량과 가격이 double이기 때문에 총 상품의 판매가격과 실제 결제된 가격의 차이가 1%이상일 경우 예외를 발생
        String errormessage = null;
        if (orderDto.getTokenAmount() > 0 && Math.abs(orderDto.getTokenPrice() - coinExchange.getTokenPrice()) / coinExchange.getTokenPrice() >= 0.05) {    //가격 변동성이 심할때 예외처리
            errormessage = "빗썸 거래소 기준으로 5프로 이상 토큰 가격 변동이 있습니다";
        } else if ((orderDto.getTotalPrice() + orderDto.getTokenAmount() * orderDto.getTokenPrice()) != orderDto.getAllProductPrice()) {
            errormessage = "총 금액 != 할인 금액 + 토큰 금액 * 토큰 수량";
        }
        if (errormessage != null) {
            throw new IllegalAccessException(errormessage);
        }

        if (orderDto.getTokenAmount() > 0) {    //유저가 포인트를 사용했다면 트랜잭션
            contractService.transferFrom(wallet.getAddress(), orderDto.getTokenAmount() * 1000);
            log.info(orderDto.getTokenAmount() * 1000 + "포인트 사용");
        }

        //유저 지갑에 필요한 수량의 포인트를 가지고 있는지 확인하고, 부족하면 예외처리
        if (Long.parseLong(contractService.balanceOf(wallet.getAddress())) < orderDto.getTokenAmount()) {
            throw new PaymentException("토큰 수량 부족으로 결제 실패");
        }

        //가격에 따른 포인트 지불
        double pointPerPrice = 0.03;
        Long pointAmountToBuyer = (Long) Math.round(orderDto.getTotalPrice() * pointPerPrice / coinExchange.getTokenPrice() * 1000);

        //구매자에게 포인트 지불
        contractService.transfer(wallet.getAddress(), pointAmountToBuyer);
        log.info(orderDto.getTotalPrice() * pointPerPrice + " = " + coinExchange.getTokenPrice() * (pointAmountToBuyer / 1000));
        return pointAmountToBuyer / 1000;
    }

    @Transactional
    public Long cancelAll(Integer memberIdx, Long orderIdx) {
        Orders orders = ordersRepository.searchOrders(orderIdx, memberIdx)
                .orElseThrow(() -> new NotExistEntityException("해당 주문내역이 없습니다"));
        try {
            iamportManager.cancelPayment(orders.getImpUid(), orders.getTotalPrice(), false, "정상적인 주문 쥐소");
            orders.cancel();
            orderProductService.removeOrderProduct(orders.getOrderProducts());
            contractService.transferFrom(orders.getMember().getWallet().getAddress(), orders.getPointAmountToBuyer() * 1000L);
        } catch (IamportResponseException e) {
            log.debug(e.getMessage());
            throw new PaymentException("아임포트 관련 문제 : 결제 실패");
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | TransactionException e) {
            log.debug(e.getMessage());
            throw new TokenTransactionException("토큰 트랜젝션 에러 발생");
        } catch (IOException e) {
            log.debug(e.getMessage());
            throw new TokenTransactionException("결제, 토큰 트랜젝션 중 기본 예외처리에 해당하지 않음");
        }
        return orders.getIdx();
    }

    public PageResultDto<Orders, OrdersDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto) {
        Page<Orders> result = ordersRepository.getOrdersByMemberIdxAndRemovedFalse(memberIdx, pageRequestDto.getPageable(Sort.by("regDate").descending()));
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    private Delivery getDeliveryInfo(OrdersDto ordersDto) {
        return Delivery.builder()
                .recipient(ordersDto.getRecipient())
                .recipientAddress(ordersDto.getRecipientAddress())
                .recipientPhone(ordersDto.getRecipientPhone())
                .deliveryMessage(ordersDto.getDeliveryMessage())
                .deliveryStatus(DeliveryStatus.READY)
                .build();
    }

    private Orders getOrderInfo(OrdersDto ordersDto, Member member, Delivery delivery) {
        return Orders.builder()
                .impUid(ordersDto.getImpUid())
                .orderNumber(ordersDto.getOrderNumber())
                .orderName(ordersDto.getOrderName())
                .totalPrice(ordersDto.getTotalPrice())
                .tokenPrice(ordersDto.getTokenPrice())
                .tokenAmount(ordersDto.getTokenAmount())
                .orderProductsCount(ordersDto.getOrderProductList().size())
                .orderState(OrderStatus.ORDER)
                .member(member)
                .delivery(delivery)
                .build();
    }

    private OrdersDto makeOrderDto(Orders orders, List<OrderProduct> orderProducts) {
        List<OrderProductDto> orderProductDtos = new ArrayList<>();

        for (OrderProduct orderProduct : orderProducts) {
            Product product = orderProduct.getProduct();
            List<ProductImageDto> imageDtos = makeImageDtos(product.getProductImages());
            OrderProductDto orderProductDto = makeOrderProductDto(orderProduct, product, imageDtos);
            orderProductDtos.add(orderProductDto);
        }

        return OrdersDto.builder()
                .orderProductList(orderProductDtos)
                .idx(orders.getIdx())
                .impUid(orders.getImpUid())
                .orderNumber(orders.getOrderNumber())
                .orderName(orders.getOrderName())
                .totalPrice(orders.getTotalPrice())
                .tokenPrice(orders.getTokenPrice())
                .tokenAmount(orders.getTokenAmount())
                .orderState(orders.getOrderState().name())
                .userEmail(orders.getMember().getEmail())
                .allProductPrice(orders.getAllProductPrice())
                .pointAmountToBuyer(orders.getPointAmountToBuyer()) // 밑에 toDto랑 차이
                .userName(orders.getMember().getName())
                .orderProductsCount(orders.getOrderProductsCount())
                .orderProductList(orders.toOrderProductDto())
                .recipient(orders.getDelivery().getRecipient())
                .recipientAddress(orders.getDelivery().getRecipientAddress())
                .recipientPhone(orders.getDelivery().getRecipientPhone())
                .deliveryStatus(orders.getDelivery().getDeliveryStatus().name())
                .deliveryMessage(orders.getDelivery().getDeliveryMessage())
                .regDate(orders.getRegDate())
                .modDate(orders.getModDate())
                .build();
    }

    private List<ProductImageDto> makeImageDtos(List<ProductImage> productImages) {
        List<ProductImageDto> imageDtos = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            imageDtos.add(productImage.toProductImageDto());
        }
        return imageDtos;
    }

    private OrderProductDto makeOrderProductDto(OrderProduct orderProduct, Product product, List<ProductImageDto> productImageDtoList) {
        return OrderProductDto.builder()
                .orderProductIdx(orderProduct.getIdx())
                .price(orderProduct.getPrice())
                .quantity(orderProduct.getQuantity())
                .isRemoved(orderProduct.isRemoved())
                .productIdx(product.getIdx())
                .title(product.getTitle())
                .brand(product.getBrand())
                .content(product.getContent())
                .imageDtoList(productImageDtoList)
                .build();
    }

    private OrdersDto entityToDto(Orders orders) {
        return OrdersDto.builder()
                .idx(orders.getIdx())
                .impUid(orders.getImpUid())
                .orderNumber(orders.getOrderNumber())
                .orderName(orders.getOrderName())
                .totalPrice(orders.getTotalPrice())
                .tokenPrice(orders.getTokenPrice())
                .tokenAmount(orders.getTokenAmount())
                .orderState(orders.getOrderState().name())
                .userEmail(orders.getMember().getEmail())
                .pointAmountToBuyer(orders.getPointAmountToBuyer())// 밑에 toDto랑 차이
                .userName(orders.getMember().getName())
                .orderProductsCount(orders.getOrderProductsCount())
                .orderProductList(orders.toOrderProductDto())
                .recipient(orders.getDelivery().getRecipient())
                .recipientAddress(orders.getDelivery().getRecipientAddress())
                .recipientPhone(orders.getDelivery().getRecipientPhone())
                .deliveryStatus(orders.getDelivery().getDeliveryStatus().name())
                .deliveryMessage(orders.getDelivery().getDeliveryMessage())
                .regDate(orders.getRegDate())
                .modDate(orders.getModDate())
                .build();
    }

}
