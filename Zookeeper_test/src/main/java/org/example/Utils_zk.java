package org.example;

import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;


public class Utils_zk {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static String host = "47.120.38.8";
    private static String port = "80";
    public CuratorFramework client = get_client();

    @SneakyThrows
    public static CuratorFramework get_client() {

        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME,
                MAX_RETRIES);
//        创建客户端实例
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(host+":"+port)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;

    }
}
