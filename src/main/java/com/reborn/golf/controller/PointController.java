package com.reborn.golf.controller;


import com.reborn.golf.api.CoinExchange;
import com.reborn.golf.dto.shop.CartListDto;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {
    private final CoinExchange coinExchange;
    @GetMapping
    public ResponseEntity<?> getprice(){
        return new ResponseEntity<>(coinExchange.getTokenPrice(), HttpStatus.OK);
    }

}
