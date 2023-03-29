package com.grozziie.grozziie_aaam;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
////InilizeSdk
///MD Ariful Islam
//// Software Engineer at THT Space
////Date : 29-3-2023
///Bluetooth SDK

public class InitilizeSdk {
    public InitilizeSdk(Context context) {

    }
    public  void SDKInitilize(Context context) {
        Dialog mDialouge=new Dialog(context);
        mDialouge.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialouge.setContentView(R.layout.initilizeallinformation);
        mDialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView escprinter=(TextView)mDialouge.findViewById(R.id.escprinter);
        escprinter.setText("Initilize SDK");

        Runnable updater;
        final  Handler myhandler=new Handler();
        updater=new Runnable() {
            @Override
            public void run() {
                escprinter.setText("Geting Data");

            }
        };
        myhandler.postDelayed(updater,6000);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(),Device_CategoryActivity.class));


            }
        },8000);
        mDialouge.create();;
        mDialouge.show();


    }
}
