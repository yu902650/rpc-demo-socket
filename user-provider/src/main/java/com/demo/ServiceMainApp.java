package com.demo;

import com.demo.service.UserServiceImpl;
import org.example.service.UserService;
import org.mmr.rpc.core.service.RPCService;

/**
 * @author bo bo
 * @Package com.demo
 * @date 2020/6/17 22:37
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @Description:
 */
public class ServiceMainApp {
    public static void main(String[] args) {
        //创建一个rpc服务端
        RPCService rpcService = new RPCService();
        //发布暴露服务
        rpcService.publishServiceAPI(UserService.class, new UserServiceImpl());
        //启动服务
        rpcService.start(39998);
    }
}
