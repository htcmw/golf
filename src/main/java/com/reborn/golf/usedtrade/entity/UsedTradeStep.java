package com.reborn.golf.usedtrade.entity;

public enum UsedTradeStep {
    //판매 요청 상태
    RESERVATION,
    //가격 제안 상태
    PROPOSAL,
    //합의된상태
    ACCEPTANCE,
    //거래 끝
    FINISH,
    //거래 취소
    CANCELED
}
