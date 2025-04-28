package com.ice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IgniteController {

    @Autowired
    private IgniteService igniteService;

    @GetMapping("/cache")
    public String cacheOperation() {
        igniteService.cacheOperation();
        return "Cache操作已执行";
    }

    @GetMapping("/cache1")
    public String cacheOperation1() {
        igniteService.cacheOperation1();
        return "Cache操作已执行";
    }

    @GetMapping("/list")
    public List<DemoEntity> list() {
        return igniteService.list();
    }
}