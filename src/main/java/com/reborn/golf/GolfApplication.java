package com.reborn.golf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
public class GolfApplication {


    public static void main(String[] args)  {
        SpringApplication.run(GolfApplication.class, args);

    }

}
