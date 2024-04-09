package org.example.zookeeper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.example.Factory;

import java.util.List;

@Slf4j
public class ZKClient {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static String host = "47.120.38.8";
    public static String port = "80";
    public CuratorFramework client = getClient();

    @SneakyThrows
    public CuratorFramework getClient() {

        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME,
                MAX_RETRIES);
//        创建客户端实例
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(host+":"+port)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        log.info("zk连接成功！");
//        addListener(zkClient);



        return zkClient;

    }

    @SneakyThrows
    public void addListener(CuratorFramework zkClient){
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, "/RPC",
                true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework,
                                                               pathChildrenCacheEvent) -> {
            System.out.println("变化: " + pathChildrenCacheEvent.getData().getPath());
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

    @SneakyThrows
    public List<String> serviceDiscover(String serviceName){

        log.info("发现服务:"+serviceName);
        return client.getChildren().forPath("/RPC/"+serviceName);
    }

    @SneakyThrows
    public void serviceRegister(String path){
        log.info("注册服务:"+path);
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);

    }
}
