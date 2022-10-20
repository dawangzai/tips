package com.maniu.ipcmaniu;

import android.util.Log;

public class PhotoService  implements IPhotoService {
    String data ;
    private static PhotoService sInstance = null;
    private PhotoService() {
    }
    //getInstance  一定 getInstance     架构写好
    public static synchronized PhotoService getInstance() {
        if (sInstance == null) {
            Log.i("maniu", "getInstance: ");
            sInstance = new PhotoService();
        }
        return sInstance;
    }

    public String getPhoto() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
