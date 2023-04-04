package com.grozziie.grozziie_aaam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import java.util.Locale;

import es.dmoral.toasty.Toasty;
////InilizeSdk
///MD Ariful Islam
//// Software Engineer at THT Space
////Date : 29-3-2023
///Bluetooth SDK

public class InitilizeSDKActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initilize_s_d_k);
        MultiDex.install(InitilizeSDKActivity.this);
        Dialog mDialouge=new Dialog(InitilizeSDKActivity.this);
        mDialouge.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialouge.setContentView(R.layout.initilizeallinformation);
        mDialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView escprinter=(TextView)mDialouge.findViewById(R.id.escprinter);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            escprinter.setText(R.string.ini_eng);
        }
        else {
            escprinter.setText(R.string.ini_china);
        }


        Runnable updater;
        final Handler myhandler=new Handler();
        updater=new Runnable() {
            @Override
            public void run() {
                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                if (defaultlanguage.toLowerCase().toString().equals("english")) {
                    escprinter.setText(R.string.geting_eng);
                }
                else {
                    escprinter.setText(R.string.geetting_chi);
                }

            }
        };
        myhandler.postDelayed(updater,3000);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                if (defaultlanguage.toLowerCase().toString().equals("english")) {
                    Toasty.success(InitilizeSDKActivity.this,"Initilize Successfully",Toasty.LENGTH_SHORT,true).show();
                }
                else {
                    escprinter.setText(R.string.geetting_chi);  Toasty.success(InitilizeSDKActivity.this,"初始化成功",Toasty.LENGTH_SHORT,true).show();
                }


                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       startActivity(new Intent(InitilizeSDKActivity.this,Device_CategoryActivity.class));
                    }
                },1000);



            }
        },4000);
        mDialouge.create();;
        mDialouge.show();

    }
}