package com.maniu.ipc;

import android.content.Intent;

import com.maniu.ipc.request.RequestBean;
import com.maniu.ipc.request.RequestParamter;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class CacheCenter {
    private static final CacheCenter ourInstance = new CacheCenter();
    public static CacheCenter getInstance() {

        return ourInstance;
    }
    private final ConcurrentHashMap<String, Class<?>> mClassMap;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Method>> mAllMethodMap;
    private final ConcurrentHashMap<String, Object> mInstanceObjectMap;
//    服务注册    服务调用  才会初始化    服务的初始化 需要1  不需要2
//    节约内存的目的
    private CacheCenter() {
        mClassMap = new ConcurrentHashMap<String, Class<?>>();
        mAllMethodMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, Method>>();
        mInstanceObjectMap = new ConcurrentHashMap<String, Object>();
    }

    public void register(String key, Class<?> clazz) {
        registerClass(key,clazz);
        registerMethod(clazz);
    }
//第一次
    private void registerMethod(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            ConcurrentHashMap<String, Method> map = mAllMethodMap.get(clazz.getName());
            if (map == null) {
                map = new ConcurrentHashMap<String, Method>();
                mAllMethodMap.put(clazz.getName(),map);
            }
//            做方法签名
            String key = getMethodParameters(method);
            map.put(method.getName(), method);
        }
    }

    public Method getMethod(RequestBean requestBean) {
        ConcurrentHashMap<String, Method> map = mAllMethodMap.get(requestBean.getClassName());
        if (map != null) {
            String key = getMethodParameters(requestBean);
            return map.get(key);
        }
        return null;

    }
    public static String getMethodParameters(RequestBean requestBean) {
        StringBuilder result = new StringBuilder();
        result.append(requestBean.getMethodName());
        if (requestBean.getRequestParamters() == null || requestBean.getRequestParamters().length == 0) {
            return result.toString();
        }
        int length = requestBean.getRequestParamters().length;
        RequestParamter[] requestParamters=requestBean.getRequestParamters();
        for (int i = 0; i < length; ++i) {
            result.append("-").append(requestParamters[i].getParameterClassName());
        }
        return result.toString();
    }


    public Class<?> getClassType(String parameterClassName) {
        try {
            Class clazz = Class.forName(parameterClassName);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    //getName     getName-String-Integer
    public static String getMethodParameters(Method method) {
        StringBuilder result = new StringBuilder();
        result.append(method.getName());
        Class<?>[] classes=method.getParameterTypes();
        int length = classes.length;
        if (length == 0) {
            return result.toString();
        }
        for (int i = 0; i < length; ++i) {
            result.append("-").append(classes[i].getName());
        }
        return result.toString();

    }

    private void registerClass(String key, Class<?> clazz) {
        mClassMap.put(key, clazz);
    }

    public void putObject(String className, Object instance) {
        mInstanceObjectMap.put(className, instance);
    }

    public Object getObject(String className) {
        return mInstanceObjectMap.get(className);
    }
}
