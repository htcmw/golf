package com.reborn.golf;

import com.reborn.golf.klaytn.HelloKAS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;

import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
public class GolfApplication {

    public static void main(String[] args) throws IOException, ApiException {
        SpringApplication.run(GolfApplication.class, args);
        HelloKAS.getBlockNumber();
        HelloKAS.createAccount();
    }

}
