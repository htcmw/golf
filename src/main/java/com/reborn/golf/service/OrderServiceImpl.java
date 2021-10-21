package com.reborn.golf.service;

import com.reborn.golf.api.CoinExchange;
import com.reborn.golf.api.ContractService;
import com.reborn.golf.api.IamportManager;
import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.exception.HighTokenPriceVolatilityException;
import com.reborn.golf.dto.exception.NotExistEntityException;
import com.reborn.golf.dto.exception.PaymentException;
import com.reborn.golf.dto.shop.OrdersDto;
import com.reborn.golf.entity.*;
import com.reborn.golf.entity.Enum.DeliveryStatus;
import com.reborn.golf.entity.Enum.OrderStatus;
import com.reborn.golf.repository.MemberRepository;
import com.reborn.golf.repository.OrderProductRepository;
import com.reborn.golf.repository.OrderRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    //유저
    private final MemberRepository memberRepository;
    //아임포트
    private final IamportManager iamportManager;
    //거래소의 코인 가격
    private final CoinExchange coinExchange;
    //클레이튼
    private final ContractService contractService;
    //OrderProduct
    private final OrderProductService orderProductService;
    private final OrderProductRepository orderProductRepository;

    @Override
    public PageResultDto<Orders, OrdersDto> getListWithUser(Integer memberIdx, PageRequestDto pageRequestDto) {
        Page<Orders> result = orderRepository.getOrdersByMemberIdxAndRemovedFalse(memberIdx, pageRequestDto.getPageable(Sort.by("regDate").descending()));
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public PageResultDto<Orders, OrdersDto> getListWithState(String state, PageRequestDto pageRequestDto) {
        Page<Orders> result = null;
        if (state.equals("ORDER")) {
            result = orderRepository.getOrdersByOrderStateAndRemovedFalse(OrderStatus.ORDER, pageRequestDto.getPageable(Sort.by("regDate").descending()));
        } else if (state.equals("DELIVERY")) {
            result = orderRepository.getOrdersByOrderStateAndRemovedFalse(OrderStatus.DELIVERY, pageRequestDto.getPageable(Sort.by("regDate").descending()));
        } else if (state.equals("CANCEL")) {
            result = orderRepository.getOrdersByOrderStateAndRemovedFalse(OrderStatus.CANCEL, pageRequestDto.getPageable(Sort.by("regDate").descending()));
        }
        Function<Orders, OrdersDto> function = (this::entityToDto);
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public OrdersDto order(Integer memberIdx, OrdersDto ordersDto) {
        //유저 정보
        Member member = memberRepository.getMemberByIdxAndRemovedFalse(memberIdx)
                .orElseThrow(() -> new NotExistEntityException("해당 유저가 없습니다"));

        if (!ordersDto.getUserEmail().equals(member.getEmail()) || !ordersDto.getUserName().equals(member.getName())) {
            log.info("잘못된 결제 유저 결제 정보입니다");
            throw new NotExistEntityException("잘못된 결제 유저 결제 정보입니다");
        }
        //지갑 정보
        Wallet wallet = member.getWallet();

        //아임포트 결제 검증
//
//        try {
//            if (!iamportManager.verificatePayment(ordersDto)){
//                iamportManager.cancelPayment(ordersDto.getImpUid(), ordersDto.getTotalPrice(), false, "결제 정보 불일치");
//            }
//        } catch (IamportResponseException | IOException e) {
//            try {
//                iamportManager.cancelPayment(ordersDto.getImpUid(), ordersDto.getTotalPrice(), false, "검증 절차상 예외 발생");
//            } catch (IamportResponseException | IOException ex) {
//                log.error("************* 결제 취소 실패 *************** : " + e.getMessage());
//            }
//            log.debug(e.getMessage());
//            throw new PaymentException("아임포트 관련 문제 : 결제 실패");
//        }

        //배송 정보
        Delivery delivery = Delivery.builder()
                .recipient(ordersDto.getRecipient())
                .recipientAddress(ordersDto.getRecipientAddress())
                .recipientPhone(ordersDto.getRecipientPhone())
                .deliveryMessage(ordersDto.getDeliveryMessage())
                .deliveryStatus(DeliveryStatus.READY)
                .build();

        //주문 정보
        Orders orders = Orders.builder()
                .impUid(ordersDto.getImpUid())
                .orderNumber(ordersDto.getOrderNumber())
                .orderState(OrderStatus.ORDER)
                .orderName(ordersDto.getOrderName())
                .totalPrice(ordersDto.getTotalPrice())
                .tokenPrice(ordersDto.getTokenPrice())
                .tokenAmount(ordersDto.getTokenAmount())
                .member(member)
                .delivery(delivery)
                .orderProductsCount(ordersDto.getOrderProductList().size())
                .build();

        orderRepository.save(orders);

        //orderProductService에서 상품들 리스트와 상품의 판매가를 가져온다
        HashMap<String, Object> map = orderProductService.makeOrderProduct(ordersDto.getOrderProductList(), orders);
        List<OrderProduct> orderProducts = (List<OrderProduct>) map.get("orderProducts");
        Integer allProductPrice = (Integer) map.get("totalPrice");

        /*
         * 포인트부분은 트랜젝션에 클레이튼을 소모하기때문에 가장 마지막에 처리
         * */

        //토큰이 수량과 가격이 double이기 때문에 총 상품의 판매가격과 실제 결제된 가격의 차이가 1%이상일 경우 예외를 발생
        double diffPrice = Math.abs(allProductPrice - (orders.getTotalPrice() + orders.getTokenAmount() * orders.getTokenPrice()));
        double comparePrice = diffPrice / allProductPrice.doubleValue() * 100;
        log.info("총 금액 = " + allProductPrice + ", 할인 금액 = " + orders.getTotalPrice() + ", 포인트 금액 = " + (orders.getTokenAmount() * orders.getTokenPrice()) + "   >>>>>  " + comparePrice + "%");

        if (diffPrice / allProductPrice.doubleValue() * 100 > 1.0) {
            throw new PaymentException("총 금액 != 할인 금액 + 토큰 금액 * 토큰 수량");
        }
        System.out.println("===============================================================================================");
        //포인트 트랜잭션
        try {
            //프론트에서 얻은 토큰 가격과 서버에서 얻은 토큰 가격 차이 비교
            double diffPointPrice = Math.abs((ordersDto.getTokenPrice() - coinExchange.getTokenPrice()) / coinExchange.getTokenPrice() * 100);
            log.info("포인트 가격 차이 : " + diffPointPrice);

            //가격 변동성이 심할때 예외처리
            if (ordersDto.getTokenAmount() > 0 && diffPointPrice >= 5.0) {
                throw new HighTokenPriceVolatilityException("빗썸 거래소 기준으로 5프로 이상 토큰 가격 변동이 있습니다");

            } else if (ordersDto.getTokenAmount() > 0) {    //유저가 포인트를 사용했다면 트랜잭션
                //유저가 포인
                contractService.transferFrom(wallet.getAddress(), ordersDto.getTokenAmount() * 1000);
                log.info(ordersDto.getTokenAmount() * 1000 + "개 포인트를 유저에게 받는다");
            }

            //가격에 따른 포인트 지불
            double pointPerPrice = 0.03;
            Long pointAmountToBuyer = (Long) Math.round(ordersDto.getTotalPrice() * pointPerPrice / coinExchange.getTokenPrice() * 1000);

            //유저 지갑에 필요한 수량의 포인트를 가지고 있는지 확인하고, 부족하면 예외처리
            if (Long.parseLong(contractService.balanceOf(wallet.getAddress())) < ordersDto.getTokenAmount()) {
                iamportManager.cancelPayment(ordersDto.getImpUid(), ordersDto.getTotalPrice(), false, "토큰 수량 부족으로 결제 실패");
                throw new PaymentException("토큰 수량 부족으로 결제 실패");
            }

            //구매자에게 포인트 지불
            contractService.transfer(wallet.getAddress(), pointAmountToBuyer);
            orders.setPointAmountToBuyer(pointAmountToBuyer/1000);
            log.info(ordersDto.getTotalPrice() * pointPerPrice + " = " + coinExchange.getTokenPrice() * (pointAmountToBuyer / 1000));

        } catch (IamportResponseException | TransactionException | IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            try {
                //포인트 트랜잭션에 실패하면 결제 취소
                iamportManager.cancelPayment(ordersDto.getImpUid(), ordersDto.getTotalPrice(), false, "토큰 관련 에러 발생");
            } catch (IamportResponseException | IOException ex) {
                log.error("************* 결제 취소 실패 *************** : " + e.getMessage());
            }
            log.debug(e.getMessage());
            throw new PaymentException("결제 실패");
        }
        return entitiesToDto(orders, orderProducts);
    }

    @Override
    @Transactional
    public Long cancel(Integer memberIdx, Long orderIdx) {

        Optional<Orders> optionalOrders = orderRepository.findById(orderIdx);

        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            orders.changeIsRemoved(true);
            orders.setOrderState(OrderStatus.CANCEL);
            orderProductService.removeOrderProduct(orders.getOrderProducts());
            orderRepository.save(orders);

            return orders.getIdx();
        }
        return null;
    }
}
