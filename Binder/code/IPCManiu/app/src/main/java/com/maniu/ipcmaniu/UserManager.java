package com.maniu.ipcmaniu;

import android.util.Log;

import com.maniu.ipcmaniu.bean.UserInfo;
//UserManager 对象   构建  ----》 getInstance  Method   .invok()  Service1 Service1
public class UserManager implements IUserManager {
    private static UserManager sInstance = null;
    private UserManager() {
    }
//getInstance  一定 getInstance     架构写好
    public static synchronized UserManager getInstance() {
        if (sInstance == null) {
            Log.i("maniu", "getInstance: ");
            sInstance = new UserManager();
        }
        return sInstance;
    }

    UserInfo userInfo;
    @Override
    public UserInfo getUser() {
        return userInfo;
    }

    @Override
    public void setUser(UserInfo user) {
        userInfo = user;
    }
}
