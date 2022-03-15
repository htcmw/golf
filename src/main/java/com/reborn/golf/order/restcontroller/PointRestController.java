package com.reborn.golf.order.restcontroller;


import com.reborn.golf.common.api.CoinExchange;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/coin")
@RequiredArgsConstructor
public class PointRestController {
    private final CoinExchange coinExchange;
    @GetMapping
    public ResponseEntity<?> getprice(){
        return new ResponseEntity<>(coinExchange.getTokenPrice(), HttpStatus.OK);
    }

}
