package com.grozziie.mygrozziie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.content.Intent;
import android.os.Bundle;

import com.grozziie.grozziie_aaam.CustomLaout;
import com.grozziie.grozziie_aaam.CustomToasty;
import com.grozziie.grozziie_aaam.InitilizeSDKActivity;
import com.grozziie.grozziie_aaam.InitilizeSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(MainActivity.this);
    startActivity(new Intent(getApplicationContext(), InitilizeSDKActivity.class));
      //  CustomToasty customToasty=new CustomToasty(MainActivity.this);
       /// customToasty.SuccessToast(MainActivity.this,"fgfgg");


    }
}