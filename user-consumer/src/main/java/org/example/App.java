package org.example;

import org.example.service.UserService;
import org.mmr.rpc.core.client.RPCClient;

import java.net.InetSocketAddress;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        UserService remoteProxy =
                RPCClient.getRemoteProxy(UserService.class, new InetSocketAddress("127.0.0.1", 39998));

        String result = remoteProxy.addUserName("zhangsan");
        System.out.println(result);
    }
}
