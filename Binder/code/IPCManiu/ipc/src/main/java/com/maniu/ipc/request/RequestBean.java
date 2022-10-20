package com.maniu.ipc.request;

public class RequestBean {
// 服务发现  1       服务调用 2
    private int type;
    private String className;
    private String methodName;  // 方法 名
    private RequestParamter[] requestParamters;

    public RequestBean() {
    }

    public RequestBean(int type, String className, String methodName, RequestParamter[] requestParamters) {
        this.type = type;
        this.className = className;
        this.methodName = methodName;
        this.requestParamters = requestParamters;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public RequestParamter[] getRequestParamters() {
        return requestParamters;
    }

    public void setRequestParamters(RequestParamter[] requestParamters) {
        this.requestParamters = requestParamters;
    }
}
