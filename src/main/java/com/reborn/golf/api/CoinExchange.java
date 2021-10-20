package com.reborn.golf.api;


import com.reborn.golf.dto.shop.TickerDto;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
@Component
public class CoinExchange implements ApplicationRunner, Runnable, ApplicationListener<ContextClosedEvent> {
    //작업을 수행할 thread
    private Thread thread;
    private boolean isShutdown = false;
    private String url;
    private RestTemplate restTemplate;

    @Getter
    private Double tokenPrice;

    //Application 시작시 실행
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(">>>>>>>>>> ApplicationRunner");
        url = "https://api.bithumb.com/public/ticker/" + "MBL" + "_" + "KRW";
        this.restTemplate = new RestTemplate();
        isShutdown = false;
        startDaemon();
    }

    //Thread
    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        try {
            while (currentThread == thread && !this.isShutdown) {
                TickerDto tickerDto = restTemplate.getForObject(new URI(url), TickerDto.class);
                if (tickerDto != null) {
                    tokenPrice = Double.parseDouble(tickerDto.getData().getClosing_price());
                    log.info(tokenPrice);
                    Thread.sleep(1000);
                }

            }
        }
        catch(InterruptedException | URISyntaxException e){
            log.debug(e.getMessage());
        }
        log.info(">>>>>>>>>> Thread to get coin price is done");
    }

    //작업을 수행한다
    private void startDaemon() {
        if (thread == null) {
            thread = new Thread(this, "CoinPriceThread.");
            thread.setDaemon(true);
        }
        if (!thread.isAlive()) {
            thread.start();
        }
    }
    //Application 종료시 실행
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.isShutdown = true;
        try {
            thread.join();
            thread = null;
            log.info(">>>>>>>>>> ContextStoppedEvent, thread is null");
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}