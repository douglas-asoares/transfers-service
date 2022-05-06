package com.bank.transfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.bank.transfers")
public class TransfersServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TransfersServiceApplication.class);
    }
}
