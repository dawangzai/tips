package com.maniu.clinetb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.maniu.ipc.ServiceManager;
import com.maniu.ipcmaniu.IPhotoService;

public class MainActivity extends AppCompatActivity {
    IPhotoService photoService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServiceManager.getDefault().open(this,"com.maniu.ipc");
    }
    public void davidService(View view) {
        photoService = ServiceManager.getDefault().getInstance(IPhotoService.class);
    }
    public void getUser(View view) {
        String data = photoService.getPhoto().toString();
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}