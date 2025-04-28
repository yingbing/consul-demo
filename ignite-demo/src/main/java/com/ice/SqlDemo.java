package com.ice;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

public class SqlDemo {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("ignite-config.xml")) {
            // 1. 创建 SQL 表（如果不存在）
            CacheConfiguration<?, ?> cacheCfg = new CacheConfiguration<>("PersonCache")
                    .setSqlSchema("PUBLIC");

            ignite.getOrCreateCache(cacheCfg).query(new SqlFieldsQuery(
                    "CREATE TABLE IF NOT EXISTS Person (" +
                            "id INT PRIMARY KEY," +
                            "name VARCHAR," +
                            "age INT)"
            )).getAll();

            // 2. 插入数据
            ignite.cache("PersonCache").query(new SqlFieldsQuery(
                    "INSERT INTO Person (id, name, age) VALUES (?, ?, ?)"
            ).setArgs(1, "John", 30));

            // 3. 执行 SQL 查询（修正后的方式）
            SqlFieldsQuery sql = new SqlFieldsQuery("SELECT name, age FROM Person WHERE age > ?")
                    .setArgs(25);

            ignite.cache("PersonCache").query(sql).getAll()
                    .forEach(row -> System.out.println("Name: " + row.get(0) + ", Age: " + row.get(1)));
        }
    }
}
