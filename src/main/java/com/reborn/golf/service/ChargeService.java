package com.reborn.golf.service;

import com.reborn.golf.dto.kakaopay.KakaoPayRequestDto;
import com.reborn.golf.dto.kakaopay.KakaoPayResponseDto;

public interface ChargeService {
    String readyPayment(KakaoPayRequestDto kakaoPayRequestDto);
    String approvePayment(String pgToken);


}
