package com.ice.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Provider (port: 8081)!";
    }
}