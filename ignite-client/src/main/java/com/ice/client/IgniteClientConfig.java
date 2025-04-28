package com.ice.client;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;

@Configuration
public class IgniteClientConfig {

    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);

        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
        discoverySpi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(discoverySpi);




        Ignite ignite = Ignition.start(cfg);

        // 配置缓存（与现有demoCache保持一致）
        CacheConfiguration<Integer, DemoEntity> cacheCfg = new CacheConfiguration<>("demoCache2");
        cacheCfg.setIndexedTypes(Integer.class, DemoEntity.class);
        cacheCfg.setBackups(-1);
        cacheCfg.setReadFromBackup(false);

        IgniteCache<Integer, DemoEntity> igniteCache = ignite.getOrCreateCache(cacheCfg);
        igniteCache.put(-1, new DemoEntity());
        igniteCache.remove(-1);


        return ignite;
    }
}