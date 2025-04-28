package com.ice;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class ComputeDemo {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("ignite-config.xml")) {
            System.out.println("\n=== 开始分布式计算演示 ===");

            // 广播任务（保持不变）
            ignite.compute().broadcast(() ->
                    System.out.println("执行节点: " + ignite.cluster().localNode().id())
            );

            // 修正后的MapReduce计算
            int totalLength = ignite.compute().execute(
                    new ComputeTaskAdapter<List<String>, Integer>() {
                        @Override
                        public Map<? extends ComputeJob, ClusterNode> map(
                                List<ClusterNode> nodes, List<String> words) {

                            return words.stream().collect(Collectors.toMap(
                                    word -> new ComputeJobAdapter() {
                                        @Override
                                        public Integer execute() {
                                            return word.length();
                                        }
                                    }, // 显式实现ComputeJob
                                    word -> nodes.get(new Random().nextInt(nodes.size()))
                            ));
                        }

                        @Override
                        public Integer reduce(List<ComputeJobResult> results) {
                            return results.stream()
                                    .mapToInt(ComputeJobResult::<Integer>getData)
                                    .sum();
                        }
                    },
                    Arrays.asList("Apache", "Ignite", "Rocks")
            );

            System.out.println("总字符数: " + totalLength);
        }
    }
}
