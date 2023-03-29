package com.grozziie.grozziie_aaam;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
////InilizeSdk
///MD Ariful Islam
//// Software Engineer at THT Space
////Date : 29-3-2023
///Bluetooth SDK

public class CustomLaout {
    public CustomLaout(Context context) {
    }

    public  void ToastyShows(Context context, String message)
    {
        int time=4000;

        View view =LayoutInflater.from(context).inflate(R.layout.customtomtoasty,null);
       TextView toastTextView = (TextView) view.findViewById(R.id.escprinter);
        toastTextView.setText(""+message);

        Toast mToast = new Toast(context);
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public  void Toastysuccess(Context context, String message)
    {
try {
    AnimationUtils animationUtils;
    Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
    Dialog mDialouge;
    mDialouge=new Dialog(context);
    mDialouge.requestWindowFeature(Window.FEATURE_NO_TITLE);
    mDialouge.setContentView(R.layout.roundcard);
    mDialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
mDialouge.show();

}catch (Exception e) {
    CustomToasty customToasty=new CustomToasty(context);
    customToasty.errorToast(context,""+e.getMessage());
}
    }
}
