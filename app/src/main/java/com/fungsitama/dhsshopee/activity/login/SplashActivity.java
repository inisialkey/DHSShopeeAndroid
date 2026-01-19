package com.fungsitama.dhsshopee.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;

public class SplashActivity extends Activity {
    SessionManager manager;

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);
        getWindow().setStatusBarColor(Color.WHITE);

        this.manager = new SessionManager();
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);


        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 3 seconds
                    sleep(3*1000);

                    // After 5 seconds redirect to another intent
                    String status=manager.getPreferences(SplashActivity.this,"status");
                    String username=manager.getPreferences(SplashActivity.this,"username");
                    Log.d("status",status);
                    Log.d("username",username);
                    if (status.equals("1")){

                            Intent i = new Intent(SplashActivity.this, NavMenuActivity.class);
                            startActivity(i);

                    }else{
                        Intent i=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(i);
                    }


                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }}
