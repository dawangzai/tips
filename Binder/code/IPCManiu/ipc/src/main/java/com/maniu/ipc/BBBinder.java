package com.maniu.ipc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BBBinder {
    public Object onTransact(Object o,Method method, Object[] parameters) {
        Object object = null;
        try {
              object = method.invoke(o, parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }
}
