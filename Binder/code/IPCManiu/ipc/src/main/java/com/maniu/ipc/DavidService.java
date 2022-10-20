package com.maniu.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.maniu.ipc.request.RequestBean;
import com.maniu.ipc.request.RequestParamter;

import java.lang.reflect.Method;

public class DavidService extends Service {
    //    服务端
    BBBinder bbBinder = new BBBinder();
    //服务发现
    public static final int TYPE_GET = 1;
    //服务调用
    public static final int TYPE_INVOKE = 2;
    Gson gson = new Gson();
    CacheCenter cacheCenter = CacheCenter.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DavidBinderInterface.Stub() {
//            主进程
            @Override
            public String transact(String reuqest) throws RemoteException {
//                服务发现  1    服务调用
                RequestBean requestBean = gson.fromJson(reuqest, RequestBean.class);
                int type = requestBean.getType();
                switch (type) {
                    case TYPE_GET:
                        Object[] parameters = makeParameterObject(requestBean);
                        Method method = cacheCenter.getMethod(requestBean);

                        Object object=bbBinder.onTransact(null,method,parameters);
                        cacheCenter.putObject(requestBean.getClassName(), object);
                        break;
                    case TYPE_INVOKE:
                        object =   cacheCenter.getObject(requestBean.getClassName());

                        Log.i("maniu", "服务调用: "+reuqest);
                        Method tempMethod = cacheCenter.getMethod(requestBean);
//                    参数    str
                        Object[] mParameters = makeParameterObject(requestBean);

                        Object result=bbBinder.onTransact(object,tempMethod,mParameters);
                        String data = gson.toJson(result);
                        return data;
                }


                return null;
            }
        };
    }

    private Object[] makeParameterObject(RequestBean requestBean) {
        Object[] mParameters = null;
        RequestParamter[] requestParamters = requestBean.getRequestParamters();
        if (requestParamters != null && requestParamters.length > 0) {
            mParameters = new Object[requestBean.getRequestParamters().length];
            for (int i = 0; i < requestParamters.length; i++) {
                RequestParamter requestParamter = requestParamters[i];
                Class<?> clazz = cacheCenter.getClassType(requestParamter.getParameterClassName());
//                    clazz   value  object
                mParameters[i] = gson.fromJson(requestParamter.getParameterValue(), clazz);
            }

        }else {
            mParameters = new Object[0];
        }
        return mParameters;
    }
}
