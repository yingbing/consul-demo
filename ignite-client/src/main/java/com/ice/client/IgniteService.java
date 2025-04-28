package com.ice.client;

import com.ice.client.repository.DemoRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgniteService {

    @Autowired
    private Ignite ignite;

    public void cacheOperation() {
        IgniteCache<Integer, String> cache = ignite.cache("demoCache");
        cache.put(1, "Hello Ignite");
        String value = cache.get(1);
        System.out.println("[Cache] Value: " + value);
    }


    @Autowired
    private DemoRepository demoRepository; // 注入仓库

    public void cacheOperation1() {
        // 使用Spring Data方式操作
        DemoEntity entity = new DemoEntity();
        entity.setId(1);
        entity.setValue("Hello Ignite");

        demoRepository.save(entity.getId(), entity);

        DemoEntity result = demoRepository.findById(1).orElse(null);
        System.out.println("[SpringData] Value: " + (result != null ? result.getValue() : "null"));

    }

    public List<DemoEntity> list() {
        return demoRepository.findByValue("Hello Ignite");
    }
}