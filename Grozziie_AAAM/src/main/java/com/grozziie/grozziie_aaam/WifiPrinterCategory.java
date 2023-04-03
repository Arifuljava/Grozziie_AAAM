package com.grozziie.grozziie_aaam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.grozziie.grozziie_aaam.wifi.WifiImagePrint;
import com.grozziie.grozziie_aaam.wifi.WifiPrintPdf;
import com.grozziie.grozziie_aaam.wifi.WifiWebPagePrint;

import java.util.Locale;

public class WifiPrinterCategory extends AppCompatActivity {
TextView imageprint,pdf_print,settingstext,web_print;
String wifi_name,wifi_ipaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_printer_category);
       //// Toast.makeText(this, ""+wifi_name, Toast.LENGTH_SHORT).show();
        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
        imageprint=findViewById(R.id.imageprint);
        pdf_print=findViewById(R.id.pdf_print);
        web_print=findViewById(R.id.web_print);
        settingstext=findViewById(R.id.settingstext);

        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            toolbar.setTitle("Printing Objects");
            imageprint.setText("Image Print");
            pdf_print.setText("Document Print");
            web_print.setText("Webpage Print");
            settingstext.setText("Print History");
        }
        else {
            toolbar.setTitle("打印对象");
            imageprint.setText("图像打印");
            pdf_print.setText("文件打印");
            web_print.setText("网页打印");
            settingstext.setText("打印历史");
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);

    }

    public void image_Print(View view) {
        if (iscunnectWifi()==true) {
           startActivity(new Intent(getApplicationContext(), WifiImagePrint.class));
        }
        else {
            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();


            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                Toast.makeText(this, "Please connect with a wifi printer", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "请连接wifi打印机", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void pdf_print(View view) {
        if (iscunnectWifi()==true) {
            startActivity(new Intent(getApplicationContext(), WifiPrintPdf.class));
        }
        else {
            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();


            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                Toast.makeText(this, "Please connect with a wifi printer", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "请连接wifi打印机", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void web_Print(View view) {
        if (iscunnectWifi()==true) {
            startActivity(new Intent(getApplicationContext(), WifiWebPagePrint.class));
        }
        else {
            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();


            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                Toast.makeText(this, "Please connect with a wifi printer", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "请连接wifi打印机", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void historyprint(View view) {
        if (iscunnectWifi()==true) {
            startActivity(new Intent(getApplicationContext(), WifiWebPagePrint.class));
        }
        else {
            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();


            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                Toast.makeText(this, "Please connect with a wifi printer", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "请连接wifi打印机", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public  Boolean iscunnectWifi() {
       boolean status=false;
        try {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo.isConnected())
            {
                status=true;
                return status;

                /*
                WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo1=wifiManager.getConnectionInfo();
                String wiifi_name11=wifiInfo1.getSSID();
                Toast.makeText(this, ""+wiifi_name11, Toast.LENGTH_SHORT).show();
                if (wifi_name.contains(wiifi_name11)) {

                }
                else {
                    status=false;
                    return status;
                }

                 */

            }
            else {
                status=false;
                return status;
            }


        }catch (Exception e) {
        }
    return null;
    }
}