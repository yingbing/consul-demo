package com.ice.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.ignite.Ignite;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IgniteNodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(IgniteNodeApplication.class, args);
    }
}