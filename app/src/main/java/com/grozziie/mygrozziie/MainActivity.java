package com.grozziie.mygrozziie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.grozziie.grozziie_aaam.CustomLaout;
import com.grozziie.grozziie_aaam.CustomToasty;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomToasty customToasty=new CustomToasty();
        CustomLaout customLaout=new CustomLaout(MainActivity.this);
        customLaout.Toastysuccess(MainActivity.this,"jkkkk");

    }
}