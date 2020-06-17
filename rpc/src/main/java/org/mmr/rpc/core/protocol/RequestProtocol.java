package org.mmr.rpc.core.protocol;

import java.io.Serializable;

/**
 * @author bo bo
 * @Package org.mmr.rpc.core.protocol
 * @date 2020/6/17 20:12
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @Description: RPC框架请求协议类，由于这类需要在网络中传输。所以需要序列化
 */
public class RequestProtocol implements Serializable {

    private String interfaceClassName; //接口全名称

    private String methodName; //方法名称

    private Class<?>[] paramertTypes; //参数类型列表

    private Object[] paramerterValues; //参数值列表

    public String getInterfaceClassName() {
        return interfaceClassName;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamertTypes() {
        return paramertTypes;
    }

    public void setParamertTypes(Class<?>[] paramertTypes) {
        this.paramertTypes = paramertTypes;
    }

    public Object[] getParamerterValues() {
        return paramerterValues;
    }

    public void setParamerterValues(Object[] paramerterValues) {
        this.paramerterValues = paramerterValues;
    }
}
