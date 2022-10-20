package com.maniu.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.maniu.ipc.request.RequestBean;
import com.maniu.ipc.request.RequestParamter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceManager {
    private static final ServiceManager ourInstance = new ServiceManager();
    DavidBinderInterface davidBinderIPCInterface;
    CacheCenter cacheCenter = CacheCenter.getInstance();
    public static ServiceManager getDefault() {
        return ourInstance;
    }
    private Context sContext;
    public void open(Context context) {
        sContext = context.getApplicationContext();
        open(context, null);
    }

    public void open(Context context, String packageName) {
        bind(context, packageName, DavidService.class);
    }
    public void bind(Context context, String packageName, Class<? extends DavidService> service) {
        Log.i("david", "bind: ");
        Intent intent ;
        DavidServiceConnection davidServiceConnection = new DavidServiceConnection();
        if (TextUtils.isEmpty(packageName)) {
//            APp内部
            intent = new Intent(context, service);
        } else {
//            APP1   App2
            ComponentName component = new ComponentName(packageName, service.getName());
            intent = new Intent();
            intent.setComponent(component);
            intent.setAction(service.getName());
        }
//        有 1 没有2 到期
        context.bindService(intent, davidServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private class DavidServiceConnection implements ServiceConnection {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("david", "onServiceConnected: ");
            davidBinderIPCInterface=  DavidBinderInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("david", "onServiceDisconnected: ");
        }
    }


    //        服务注册   服务 注册
    public void add(String key, Class<?> clazz) {
        cacheCenter.register(key,clazz);
    }

    //----------------------------------A进程 ------张三   李四------------------



    private static final Gson GSON = new Gson();
//    ---------------------------B进程-----------------------
//
    public <T> T getInstance(Class<T> clazz, Object... parameters) {
//        服务发现  实际上是吧需要调用的服务   json  发送出去      请求
//        getInstance
        sendRequest(clazz, null, parameters, DavidService.TYPE_GET);
//        没有返回
//        客户端  内容  动态代理
//    假装返回
        return getProxy(clazz);
    }

    private <T> T getProxy(Class<T> clazz) {
        ClassLoader classLoader = sContext.getClassLoader();
//代理处理器
        return (T) Proxy.newProxyInstance(classLoader,new Class[]{clazz},  new BpBinder(clazz));
    }

    //    什么时候   F进程
//    服务发现 1    服务调用2
    public  <T> String sendRequest(Class<T> clazz, Method method, Object[] parameters, int type) {
        RequestParamter[] requestParamters = null;
        if (parameters != null && parameters.length >0) {
            requestParamters = new RequestParamter[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = GSON.toJson(parameter);
                RequestParamter requestParamter = new RequestParamter(parameterClassName, parameterValue);
                requestParamters[i] = requestParamter;
            }

        }

        String className = clazz.getAnnotation(ClassId.class).value();
        String methodName =method==null ? "getInstance" : method.getName();
//        RequestBean requestBean = new RequestBean();
//        requestBean.setClassName(className);
        RequestBean requestBean = new RequestBean(type, className,
                methodName, requestParamters);
        String request = GSON.toJson(requestBean);
//Binder    copy_form_user     物理内存  -----》mmap函数  Service
        Log.i("david", "请求  " + request);
        try {
            String responce=davidBinderIPCInterface.transact(request);
            return responce;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
