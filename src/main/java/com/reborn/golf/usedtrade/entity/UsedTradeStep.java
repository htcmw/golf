package com.reborn.golf.usedtrade.entity;

public enum UsedTradeStep {
    RESERVATION,    //판매 요청 상태
    PROPOSAL,   //가격 제안 상태
    ACCEPTANCE, //합의된상태
    FINISH, //거래 끝
    CANCELED    //거래 취소
}
