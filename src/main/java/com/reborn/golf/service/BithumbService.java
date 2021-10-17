package com.reborn.golf.service;

import com.reborn.golf.dto.TickerDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
public class BithumbService {
    private final String url;
    private final RestTemplate restTemplate;
    public BithumbService(String orderCurrency, String paymentCurrency) {
        url = "https://api.bithumb.com/public/ticker/" + orderCurrency + "_" + paymentCurrency;
        this.restTemplate = new RestTemplate();
    }

    public TickerDto connect() {
        try {
            return restTemplate.getForObject(new URI(url), TickerDto.class);
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
            return null;
        }

    }

}
