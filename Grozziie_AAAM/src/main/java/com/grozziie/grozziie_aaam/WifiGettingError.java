package com.grozziie.grozziie_aaam;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.grozziie.grozziie_aaam.wifi.Wifi;

public class WifiGettingError {
    Context context;

    public WifiGettingError(Context context) {
        this.context = context;
    }
    public  void  ischeckinError(String ip_address) {
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        String wifiname=wifiInfo.getSSID();
        String originalString = wifiname;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < originalString.length(); i++) {
            char c = originalString.charAt(i);
            if (c != '"') {
                builder.append(c);
            }
        }

        String newString = builder.toString();

// newString now contains "Hell Wrld"


        Toast.makeText(context, ""+newString, Toast.LENGTH_SHORT).show();
    }
}
