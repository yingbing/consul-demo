以下是完整的可运行项目结构和代码，包含Maven配置和详细执行说明：

项目结构
复制
ignite-demo/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── CacheDemo.java
│       │           ├── ComputeDemo.java
│       │           ├── SqlDemo.java
│       │           └── RunAll.java
│       └── resources/
│           └── ignite-config.xml
├── pom.xml
└── README.md
1. Maven配置 (pom.xml)



<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
http://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>ignite-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <ignite.version>2.14.0</ignite.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.ignite</groupId>
            <artifactId>ignite-core</artifactId>
            <version>${ignite.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.ignite</groupId>
            <artifactId>ignite-spring</artifactId>
            <version>${ignite.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.example.RunAll</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
运行 HTML
2. Ignite配置 (ignite-config.xml)
xml
复制
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="ignite.cfg" class="org.apache.ignite.configuration.IgniteConfiguration">
        <!-- 集群名称 -->
        <property name="igniteInstanceName" value="demoCluster"/>
        
        <!-- 内存配置 -->
        <property name="dataStorageConfiguration">
            <bean class="org.apache.ignite.configuration.DataStorageConfiguration">
                <property name="defaultDataRegionConfiguration">
                    <bean class="org.apache.ignite.configuration.DataRegionConfiguration">
                        <property name="name" value="Default_Region"/>
                        <property name="initialSize" value="#{100 * 1024 * 1024}"/> <!-- 100MB -->
                        <property name="maxSize" value="#{1 * 1024 * 1024 * 1024}"/> <!-- 1GB -->
                    </bean>
                </property>
            </bean>
        </property>
        
        <!-- 自动发现配置 -->
        <property name="discoverySpi">
            <bean class="org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi">
                <property name="ipFinder">
                    <bean class="org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder">
                        <property name="addresses">
                            <list>
                                <value>127.0.0.1:47500..47509</value>
                            </list>
                        </property>
                    </bean>
                </property>
            </bean>
        </property>
    </bean>
</beans>
运行 HTML
3. 示例代码
CacheDemo.java
java
复制
package com.example;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.proxy.IgniteClientConfiguration;

public class CacheDemo {
public static void main(String[] args) {
try (Ignite ignite = Ignition.start("ignite-config.xml")) {
System.out.println("\n=== 开始分布式缓存演示 ===");

            IgniteCache<Integer, String> cache = ignite.getOrCreateCache("demoCache");
            
            // 基础操作
            cache.put(1, "Hello");
            cache.put(2, "Ignite!");
            System.out.println("键值存储结果: " + cache.get(1) + " " + cache.get(2));
            
            // SQL查询
            cache.query(new org.apache.ignite.cache.query.SqlFieldsQuery(
                "CREATE TABLE IF NOT EXISTS Word (_key INT PRIMARY KEY, _val VARCHAR)"
            )).getAll();
            
            cache.query(new org.apache.ignite.cache.query.SqlFieldsQuery(
                "INSERT INTO Word (_key, _val) VALUES (1, 'Hello'), (2, 'Ignite')"
            )).getAll();
            
            System.out.println("SQL查询结果:");
            cache.query(new org.apache.ignite.cache.query.SqlFieldsQuery(
                "SELECT _val FROM Word WHERE _key > 1"
            )).getAll().forEach(System.out::println);
        }
    }
}
ComputeDemo.java
java
复制
package com.example;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTask;

public class ComputeDemo {
public static void main(String[] args) {
try (Ignite ignite = Ignition.start("ignite-config.xml")) {
System.out.println("\n=== 开始分布式计算演示 ===");

            // 广播任务
            ignite.compute().broadcast(() -> 
                System.out.println("执行节点: " + ignite.cluster().localNode().id())
            );
            
            // MapReduce计算
            int totalLength = ignite.compute().apply(
                String::length,
                java.util.Arrays.asList("Apache", "Ignite", "Rocks"),
                (results) -> results.stream().mapToInt(Integer::intValue).sum()
            );
            
            System.out.println("总字符数: " + totalLength);
        }
    }
}
SqlDemo.java
java
复制
package com.example;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;

public class SqlDemo {
public static void main(String[] args) {
try (Ignite ignite = Ignition.start("ignite-config.xml")) {
System.out.println("\n=== 开始SQL引擎演示 ===");

            // 创建表
            ignite.sql().execute(null, 
                "CREATE TABLE IF NOT EXISTS City (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR, " +
                "region VARCHAR) WITH \"template=partitioned\"");
            
            // 插入数据
            ignite.sql().execute(null, 
                "INSERT INTO City (id, name, region) VALUES " +
                "(1, 'San Francisco', 'CA'), " + 
                "(2, 'New York', 'NY')");
            
            // 查询数据
            System.out.println("加利福尼亚州城市:");
            try (var cursor = ignite.sql().execute(null, 
                "SELECT name, region FROM City WHERE region = 'CA'")) {
                cursor.forEach(row -> 
                    System.out.println(row.get(0) + " - " + row.get(1))
                );
            }
        }
    }
}
RunAll.java (入口类)
java
复制
package com.example;

public class RunAll {
public static void main(String[] args) throws Exception {
CacheDemo.main(args);
ComputeDemo.main(args);
SqlDemo.main(args);
}
}
4. 运行说明
   构建项目：

bash
复制
mvn clean package
运行全部演示：

bash
复制
java -jar target/ignite-demo-1.0-SNAPSHOT.jar
单独运行某个演示：

bash
复制
java -cp target/ignite-demo-1.0-SNAPSHOT.jar com.example.CacheDemo
java -cp target/ignite-demo-1.0-SNAPSHOT.jar com.example.ComputeDemo
java -cp target/ignite-demo-1.0-SNAPSHOT.jar com.example.SqlDemo
5. 预期输出
   复制
   === 开始分布式缓存演示 ===
   键值存储结果: Hello Ignite!
   SQL查询结果:
   Ignite

=== 开始分布式计算演示 ===
执行节点: [节点ID]
执行节点: [节点ID]
总字符数: 16

=== 开始SQL引擎演示 ===
加利福尼亚州城市:
San Francisco - CA
项目特点：
即开即用：内置本地集群配置，无需额外设置

资源隔离：每个演示使用独立的方法，避免干扰

配置灵活：通过XML文件可调整内存/网络参数

日志可见：自动显示节点操作日志，便于演示观察

如果需要更复杂的配置（如持久化存储、机器学习集成），可以基于此项目结构扩展。