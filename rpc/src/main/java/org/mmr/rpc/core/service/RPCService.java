package org.mmr.rpc.core.service;

import org.mmr.rpc.core.protocol.RequestProtocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author bo bo
 * @Package org.mmr.rpc.core.service
 * @date 2020/6/17 19:54
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @Description: RPC框架服务的核心实现类
 * 1、暴露调用服务接口
 * 2、启动服务端
 */
public class RPCService {
    //定义储存暴露服务列表
    Map<String, Object> serverMap = new ConcurrentHashMap<>(32);
    //定义一个线程池
    ThreadPoolExecutor  poolExecutor = new ThreadPoolExecutor(
            4,
            10,
            200,
            TimeUnit.MICROSECONDS,
            new ArrayBlockingQueue<>(10));

    /**
     * 定义暴露服务的方法
     * @param interfaceClass
     * @param instance
     */
    public void publishServiceAPI(Class<?> interfaceClass, Object instance) {
        this.serverMap.put(interfaceClass.getName(),instance);
    }

    /**
     * 定义发布服务的方法
     * @param port
     */
    public void start(int port){

        try {
            //创建网络服务端
            ServerSocket serverSocket = new ServerSocket();
            System.out.println("new Port" + port);
            //绑定指定端口
            serverSocket.bind(new InetSocketAddress(port));
            System.err.println("====== RPC START ....... =======");

            while (true){
                poolExecutor.execute(new ServerTask(serverSocket.accept()));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建一个客户端请求处理的线程类
     */
    private class ServerTask implements Runnable{

        private final Socket socket;

        public ServerTask(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    ObjectInputStream deSerial = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream serial = new ObjectOutputStream(socket.getOutputStream());
                    ){
                //反序列化 获取客户端传入数据
                RequestProtocol requestProtocol = (RequestProtocol) deSerial.readObject();
                // 获取接口全名称
                String interfaceName = requestProtocol.getInterfaceClassName();
                Object instance = serverMap.get(interfaceName);
                if (null == instance){
                    return;
                }
                //创建一个方法对象 (反射)
                Method method = instance.getClass().getDeclaredMethod(requestProtocol.getMethodName()
                        , requestProtocol.getParamertTypes());

                //调用方法
                Object result = method.invoke(instance, requestProtocol.getParamerterValues());

                //序列化调用结果
                serial.writeObject(result);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
