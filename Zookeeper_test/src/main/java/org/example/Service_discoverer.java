package org.example;

import org.apache.curator.framework.CuratorFramework;

public class Service_discoverer {

    public static String get_addr(String name) {
        //获取客户端
        CuratorFramework client = Utils_zk.
        //根据服务名获取子节点/RPC/服务名
        //根据负载均衡返回一个addr
    }

}
