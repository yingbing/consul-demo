package com.ice;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.configuration.CacheConfiguration;

public class CacheDemo {
    // 实体类定义
    public static class Word {
        @QuerySqlField(index = true)
        private int id;

        @QuerySqlField
        private String content;

        public Word(int id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    public static void main(String[] args) {
        // 配置支持SQL的缓存
        CacheConfiguration<Integer, Word> cfg = new CacheConfiguration<>("demoCache1");
        cfg.setIndexedTypes(Integer.class, Word.class);
        cfg.setCacheMode(CacheMode.PARTITIONED);

        try (Ignite ignite = Ignition.start("ignite-config.xml")) {
            System.out.println("\n=== 开始增强版缓存演示 ===");

            // 创建带类型定义的缓存
            IgniteCache<Integer, Word> cache = ignite.getOrCreateCache(cfg);

            // 1. 创建表结构（使用显式DDL）
//            executeSql(cache,
//                    "CREATE TABLE IF NOT EXISTS Word (" +
//                            "id INT PRIMARY KEY, " +
//                            "content VARCHAR) " +
//                            "WITH \"template=partitioned,CACHE_NAME=demoCache1\"");

//            // 2. 插入数据（单条插入保证主键）
//            executeSql(cache,
//                    "INSERT INTO Word (id, content) VALUES (?, ?)",
//                    1, "Hello");
//
//            executeSql(cache,
//                    "INSERT INTO Word (id, content) VALUES (?, ?)",
//                    2, "Ignite");
//
//            // 3. 查询数据
//            System.out.println("\n自定义列查询结果：");
//            executeSqlQuery(cache,
//                    "SELECT content FROM Word WHERE id > ?",
//                    1);

            // 4. 实体类操作演示
            cache.put(3, new Word(3, "Java"));
            System.out.println("\n实体类查询结果：" + cache.get(3).content);
        } catch (Exception e) {
            System.err.println("操作异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // SQL执行辅助方法
    private static void executeSql(IgniteCache<?, ?> cache, String sql, Object... args) {
        cache.query(new SqlFieldsQuery(sql).setArgs(args)).getAll();
    }

    // SQL查询辅助方法
    private static void executeSqlQuery(IgniteCache<?, ?> cache, String sql, Object... args) {
        cache.query(new SqlFieldsQuery(sql).setArgs(args))
                .getAll()
                .forEach(row -> System.out.println("  " + row.get(0)));
    }
}
