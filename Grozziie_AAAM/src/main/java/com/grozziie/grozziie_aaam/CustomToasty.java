package com.grozziie.grozziie_aaam;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
////InilizeSdk
///MD Ariful Islam
//// Software Engineer at THT Space
////Date : 29-3-2023
///Bluetooth SDK

public class CustomToasty {
    public CustomToasty(Context context) {

    }
    public  void SuccessToast(Context context,String message) {
        Toasty.success(context,""+message,Toast.LENGTH_SHORT,true).show();
        return;
    }
    ///error
    public  void errorToast(Context context,String message) {
        Toasty.error(context,""+message,Toast.LENGTH_SHORT,true).show();
        return;
    }
    //warning
    public  void warningToast(Context context,String message) {
        Toasty.warning(context,""+message,Toast.LENGTH_SHORT,true).show();
        return;
    }
    //info
    public  void infoToast(Context context,String message) {
        Toasty.info(context,""+message,Toast.LENGTH_SHORT,true).show();
        return;
    }
    //delete
    public  void deleteToast(Context context,String message) {
        Toasty.error(context,""+message,Toast.LENGTH_SHORT,true).show();
        return;
    }
    //normal
    public  void normalToast(Context context,String message) {
        Toasty.normal(context,""+message,Toast.LENGTH_SHORT).show();
        return;
    }



    public  void MyToasty(Context context, String messge) {
        Toast.makeText(context, ""+messge, Toast.LENGTH_SHORT).show();
    }
}
