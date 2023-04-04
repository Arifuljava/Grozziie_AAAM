package com.grozziie.grozziie_aaam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.print.PrintHelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.grozziie.grozziie_aaam.wifi.WifiImagePrint;
import com.grozziie.grozziie_aaam.wifi.WifiPrintPdf;
import com.grozziie.grozziie_aaam.wifi.WifiWebPagePrint;
import com.grozziie.grozziie_aaam.wifi.Wifi_CreatePdfActivity;

import java.util.Locale;

public class WifiPrinterCategory extends AppCompatActivity {
TextView imageprint,pdf_print,settingstext,web_print;
String wifi_name,wifi_ipaddress;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_printer_category);
        /////webpage loading
        webView=findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //if WebView load successfully then VISIBLE fab Button

            }
        });
        webView.loadUrl("https://www.google.com/");

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
        ////my wifi getting
        wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        if (wifiInfo.getSSID().toLowerCase().equals("acx_695")) {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        }
        else {

        }


    }
    WifiManager wifiManager;

    public void image_Print(View view) {
        if (iscunnectWifi()==true) {
         ///  startActivity(new Intent(getApplicationContext(), WifiImagePrint.class));
            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();


            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                String listt[]={"Print Test Image","Print From Galary"};
                AlertDialog.Builder builder=new AlertDialog.Builder(WifiPrinterCategory.this);
                builder.setTitle("Printing  Options")
                        .setItems(listt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0)
                                {
                                    Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.imagepost);

                                    PrintHelper photoPrinter = new PrintHelper(WifiPrinterCategory.this);
                                    photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                                    photoPrinter.printBitmap("Testing_image.jpg "+"-  print", bitmap1);

                                }
                                else {
                                    startActivity(new Intent(getApplicationContext(),WifiImagePrint.class));
                                }

                            }
                        }).create();
                builder.show();

            }
            else {
                String listt[]={"打印测试图像","从 Galary 打印"};
                AlertDialog.Builder builder=new AlertDialog.Builder(WifiPrinterCategory.this);
                builder.setTitle("打印选项")
                        .setItems(listt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0)
                                {
                                    Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.imagepost);

                                    PrintHelper photoPrinter = new PrintHelper(WifiPrinterCategory.this);
                                    photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                                    photoPrinter.printBitmap("测试图片.jpg "+"-  print", bitmap1);

                                }
                                else {
                                    startActivity(new Intent(getApplicationContext(),WifiImagePrint.class));
                                }

                            }
                        }).create();
                builder.show();

            }



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
            /////////
            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();


            if (defaultlanguage.toLowerCase().toString().equals("english")) {

                String op[]={"Print Testing PDF","Print From File"};

                AlertDialog.Builder builder=new AlertDialog.Builder(WifiPrinterCategory.this);
                builder.setTitle("Pdf Printing Section")
                        .setItems(op, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0) {
                                }
                                else if(which==1) {
                                    startActivity(new Intent(getApplicationContext(),WifiPrintPdf.class));

                                }

                            }
                        }).create();
                builder.show();

            }
            else {

                String op[]={"打印测试 PDF","从文件打印"};

                AlertDialog.Builder builder=new AlertDialog.Builder(WifiPrinterCategory.this);
                builder.setTitle("PDF打印部")
                        .setItems(op, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0) {
                                }
                                else if(which==1) {
                                }

                            }
                        }).create();
                builder.show();

            }


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
            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();


            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                String list[]={"Print Test Webpage","Print Real Web Page"};

                AlertDialog.Builder builder=new AlertDialog.Builder(WifiPrinterCategory.this);
                builder.setTitle("Webpage Printing Options")
                        .setItems(list, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0) {
GotoPrint();;
                                }
                                else {
                                    startActivity(new Intent(getApplicationContext(),WifiWebPagePrint.class));
                                }

                            }
                        }).create();
                builder.show();
            }
            else {
                String list[]={"打印测试网页","打印真实网页"};

                AlertDialog.Builder builder=new AlertDialog.Builder(WifiPrinterCategory.this);
                builder.setTitle("网页打印选项")
                        .setItems(list, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0) {
                                    GotoPrint();;
                                }
                                else {
                                    startActivity(new Intent(getApplicationContext(),WifiWebPagePrint.class));
                                }

                            }
                        }).create();
                builder.show();
            }

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
public  void GotoPrint() {

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