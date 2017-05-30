package com.example.hatio.insidepositioning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnCamera, btnLogin, btnStart, btnSetting, btnMonoVo;

    private static final String TAG = "MainActivity";

    static {
        if(!OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = (Button)findViewById(R.id.btn_camera);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnStart = (Button)findViewById(R.id.btn_start);
        btnSetting = (Button)findViewById(R.id.btn_setting);
        btnMonoVo = (Button)findViewById(R.id.btn_monovo);

        btnCamera.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnMonoVo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()){
            case R.id.btn_camera:
                i = new Intent(this, CameraView.class);
                break;
            case R.id.btn_login:
                i = new Intent(this, Login.class);
                break;
            case R.id.btn_start:
                i = new Intent(this, OdometerCamera.class);
                break;
            case R.id.btn_setting:
                i = new Intent(this, Setting.class);
                break;
            case R.id.btn_monovo:
                i = new Intent(this, MonoVo.class);
                break;
        }

        startActivity(i);


    }


}
