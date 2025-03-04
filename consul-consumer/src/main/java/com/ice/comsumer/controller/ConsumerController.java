package com.ice.comsumer.controller;


import com.ice.comsumer.client.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    @Autowired
    private ProviderClient providerClient;  // 注入 Feign 客户端

    @GetMapping("/call")
    public String callProvider() {
        return providerClient.hello();
    }
}