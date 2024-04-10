package org.example;

import org.example.netty.Client;
import org.example.netty.Server;
import org.example.netty.ServicePrivider;
import org.example.netty.UnprocessedRequests;
import org.example.serialize.Serializer;
import org.example.zookeeper.ZKClient;

public class Factory {
    public static Client client = null;
    public static UnprocessedRequests unprocessedRequests = new UnprocessedRequests();
    public static ZKClient zkClient = new ZKClient();
    public static ServicePrivider servicePrivider = new ServicePrivider();
    public static Server server = null;
    public static Serializer serializer = new Serializer();
}
