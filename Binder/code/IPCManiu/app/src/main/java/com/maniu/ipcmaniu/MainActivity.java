package com.maniu.ipcmaniu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maniu.ipc.DavidService;
import com.maniu.ipc.ServiceManager;
import com.maniu.ipcmaniu.bean.UserInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServiceManager.getDefault().add("UserManager", UserManager.class);

        ServiceManager.getDefault().add("PhotoService", PhotoService.class);
        PhotoService.getInstance().setData("这是我和99号技师的照片");
//        服务Service
        UserManager.getInstance().setUser(new UserInfo("david11111", "123456"));
    }
    public void change(View view) {
        startActivity(new Intent(this,SecondActivity.class));
    }
}