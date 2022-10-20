package com.maniu.ipcmaniu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.maniu.ipc.ServiceManager;

public class SecondActivity extends Activity {
    IUserManager userManager;
    IPhotoService photoService;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ServiceManager.getDefault().open(this);

    }
//    服务发现
    public void davidService(View view) {
//        能 1 不能  2 IUserManager
//        userManager
        userManager=ServiceManager.getDefault().getInstance(IUserManager.class);
//        getUser
        photoService = ServiceManager.getDefault().getInstance(IPhotoService.class);
//        photoService  动态代理
    }

    public void getUser(View view) {
        Toast.makeText(this,"-----> "+photoService.getPhoto(), Toast.LENGTH_SHORT).show();
    }
}
