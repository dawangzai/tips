package com.maniu.ipcmaniu;

import com.maniu.ipc.ClassId;
import com.maniu.ipcmaniu.bean.UserInfo;

//客户端服务的
@ClassId("com.maniu.ipcmaniu.UserManager")
public interface IUserManager {
//
    public UserInfo getUser();

    public void setUser(UserInfo user);

}
