package com.ice.client.repository;

import com.ice.client.DemoEntity;
import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.Query;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;


import java.util.List;

@RepositoryConfig(cacheName = "demoCache2")
public interface DemoRepository extends IgniteRepository<DemoEntity, Integer> {
    // 自定义查询方法示例
    List<DemoEntity> findByValue(String value);

}
