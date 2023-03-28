package com.grozziie.grozziie_aaam;

import android.content.Context;
import android.widget.Toast;

public class CustomToasty {
    public  void MyToasty(Context context,String messge) {
        Toast.makeText(context, ""+messge, Toast.LENGTH_SHORT).show();
    }
}
