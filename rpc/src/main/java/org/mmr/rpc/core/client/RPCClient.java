package org.mmr.rpc.core.client;

import org.mmr.rpc.core.protocol.RequestProtocol;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author bo bo
 * @Package org.mmr.rpc.core.client
 * @date 2020/6/17 19:54
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @Description: RPC框架的客户端核心实现类
 */
public class RPCClient {

    /**
     * 获取远程调用后返回接口具体的结果/ 通过动态代理获取调用接口对应实例
     *
     * @param interfaceClass
     * @param inetSocketAddress
     * @param <T>
     * @return
     */
    public static <T> T getRemoteProxy(Class<T> interfaceClass, InetSocketAddress inetSocketAddress) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        try (Socket socket = new Socket()) {
                            //通过网络连接客户端
                            socket.connect(inetSocketAddress);
                            try (
                                    //获取输出流
                                    ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream());
                                    //获取输入流
                                    ObjectInputStream deSerializer = new ObjectInputStream(socket.getInputStream())
                            ) {
                                //创建一个rpc框架中请求协议对象
                                RequestProtocol requestProtocol = new RequestProtocol();
                                //填充属性
                                requestProtocol.setInterfaceClassName(interfaceClass.getName());
                                requestProtocol.setMethodName(method.getName());
                                requestProtocol.setParamertTypes(method.getParameterTypes());
                                requestProtocol.setParamerterValues(args);
                                // 序列化协议对象(放入到网络中）
                                serializer.writeObject(requestProtocol);
                                // 反序列化（服务端放入的数据获取出来）
                                Object result = deSerializer.readObject();
                                return result;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });


    }

}
