package com.maniu.ipc;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
//发送角色     BpBinder类
public class BpBinder implements InvocationHandler {

    private Class clazz;
    private static final Gson GSON = new Gson();
    public BpBinder(Class clazz) {

        this.clazz = clazz;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        java  getUser------> 序列化的信息
        Log.i("David", "invoke:-==-0--- " + method.getName());
        String data =   ServiceManager.getDefault().sendRequest(clazz, method, args, DavidService.TYPE_INVOKE);
        if (!TextUtils.isEmpty(data)) {
            Object object= GSON.fromJson(data, method.getReturnType());
            return object;
        }
        return null;
    }
}
