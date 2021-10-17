package com.reborn.golf.api;


import com.reborn.golf.dto.TickerDto;
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
    private static Integer coinPrice;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(">>>>>>>>>> ApplicationRunner");
        url = "https://api.bithumb.com/public/ticker/" + "BTC" + "_" + "KRW";
        this.restTemplate = new RestTemplate();
        isShutdown = false;
        startDaemon();
    }

    //스레드가 실제로 작업하는 부분
    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        try {
            while (currentThread == thread && !this.isShutdown) {
                TickerDto tickerDto = restTemplate.getForObject(new URI(url), TickerDto.class);
                if (tickerDto != null) {
                    coinPrice = Integer.parseInt(tickerDto.getData().getClosing_price());
                    log.info(coinPrice);
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