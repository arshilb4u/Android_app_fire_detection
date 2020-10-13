package com.example.arun.firealert;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.List;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
         if(isSerivceRunning(getApplicationContext(),ExampleService.class))
        {

        }
        else
        {
            Intent serviceIntent=new Intent(SplashScreen.this,ExampleService.class);
            startService(serviceIntent);
        }
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
    public boolean isSerivceRunning(Context c,Class<?> serviceClass)
    {
        ActivityManager activityManager=(ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services=activityManager.getRunningServices(Integer.MAX_VALUE);

        for(ActivityManager.RunningServiceInfo runningServiceInfo:services)
        {
            if(runningServiceInfo.service.getClassName().equals(serviceClass.getName()))
            {
                return true;
            }
        }
        return false;
    }

}
