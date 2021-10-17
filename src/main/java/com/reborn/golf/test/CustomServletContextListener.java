package com.reborn.golf.test;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@Log4j2
public class CustomServletContextListener implements ServletContextListener, Runnable {
    //작업을 수행할 thread
    private Thread thread;
    private boolean isShutdown = false;

    //context
    private ServletContext sc;

    //작업을 수행한다
    public void startDaemon() {
        if (thread == null) {
            thread = new Thread(this, "CoinPriceThread.");
            thread.setDaemon(true);
        }
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    //스레드가 실제로 작업하는 부분
    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        while (currentThread == thread && !this.isShutdown) {
            try {
                log.info(">>>>>>>>>> Thread to get coin price is running");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info(">>>>>>>>>> Thread to get coin price is done");
    }

    //컨텍스트 초기화 시 데몬 스레드를 작동한다
    @Override
    public void contextInitialized(ServletContextEvent event) {
        sc = event.getServletContext();
        startDaemon();
    }

    //컨텍스트 종료 시 thread를 종료시킨다
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        this.isShutdown = true;
        try {
            thread.join();
            thread = null;
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
