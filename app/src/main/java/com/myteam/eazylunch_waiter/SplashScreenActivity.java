package com.myteam.eazylunch_waiter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setStatusBarColor(ContextCompat.getColor(SplashScreenActivity.this,R.color.primary));
        auth = FirebaseAuth.getInstance();
        System.out.println(auth.getCurrentUser());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,ValidWaiterActivity.class);
                   startActivity(intent);
//                if(auth.getCurrentUser()==null){
//                    Intent intent = new Intent(SplashScreenActivity.this,SignIn.class);
//                    startActivity(intent);
//                }else{
//                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
//                }
                finish();
            }
        },1000);
    }
}