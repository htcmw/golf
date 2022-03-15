package com.reborn.golf.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickerDto {
    //결과 상태 코드 (정상: 0000, 그 외 에러 코드 참조)
    private String status;
    private DataDto data;

    @Getter
    @Setter
    public static class DataDto {
        //시가 00시 기준
        private String opening_price;
        //종가 00시 기준
        private String closing_price;
        //저가 00시 기준
        private String min_price;
        //고가 00시 기준
        private String max_price;
        //거래량 00시 기준
        private String units_traded;
        //거래금액 00시 기준
        private String acc_trade_value;
        //전일종가
        private String prev_closing_price;
        //최근 24시간 거래량
        private String units_traded_24H;
        //최근 24시간 거래금액
        private String acc_trade_value_24H;
        //최근 24시간 변동가
        private String fluctate_24H;
        //최근 24시간 변동률
        private String fluctate_rate_24H;
        //타임 스탬프
        private String date;
    }
}

