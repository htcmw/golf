package com.reborn.golf.test;

import com.reborn.golf.dto.TickerDto;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
@Component
public class BithumbService {
//    private String url;
//    private RestTemplate restTemplate;
//    private boolean isShutdown = false;
//
//    @Getter
//    private static Integer coinPrice;
//
//
//    @Override
//    public void run() {
//        log.info(">>>>>>>>>> Thread to get coin price is running");
//        try {
//            while (true) {
//                TickerDto tickerDto = restTemplate.getForObject(new URI(url), TickerDto.class);
//                if (tickerDto != null) {
//                    coinPrice = Integer.parseInt(tickerDto.getData().getClosing_price());
//                    log.info(coinPrice);
//                log.info("============================================================================");
//                if(Thread.interrupted())
//                    break;
//                Thread.sleep(1000);
//
//            }
//
//        } catch (InterruptedException e) {
//            log.debug(e.getMessage());
//        }
//    }
//
//    public Runnable init(String orderCurrency, String paymentCurrency) {
//        url = "https://api.bithumb.com/public/ticker/" + orderCurrency + "_" + paymentCurrency;
//        this.restTemplate = new RestTemplate();
//        coinPrice = null;
//        isShutdown = false;
//        return this;
//    }
}
