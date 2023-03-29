package com.grozziie.grozziie_aaam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;

import com.tapadoo.alerter.Alert;

import java.util.List;

import es.dmoral.toasty.Toasty;
////InilizeSdk
///MD Ariful Islam
//// Software Engineer at THT Space
////Date : 29-3-2023
///Bluetooth SDK

public class Device_CategoryActivity extends AppCompatActivity {

BluetoothAdapter bluetoothAdapter;
BluetoothSocket bluetoothSocket;
BluetoothDevice bluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device__category);
        /////initilize all things
        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        toolbar.setTitle("Device Category");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar();

        try {
            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();



        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bluetooth(View view) {
        try {
            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
            if (!bluetoothAdapter.isEnabled()) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Device_CategoryActivity.this);
                builder.setTitle("Bluetooth")
                        .setMessage("Your bluetooth is disable now.\nDo you want to enable it?")
                        .setPositiveButton("NOT NOW", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }).setNegativeButton("ENABLE NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        bluetoothAdapter.enable();
                        bluetoothAdapter.enable();


                    }
                }).create();
                builder.show();
            }
            else {
                startActivity(new Intent(getApplicationContext(),BluetoothList.class));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wifi(View view) {

        ConnectivityManager cm = (ConnectivityManager) Device_CategoryActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()) {
            WifiManager wifiManager = (WifiManager) Device_CategoryActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(true);
        }
        else {
            AlertDialog.Builder builder=new AlertDialog.Builder(Device_CategoryActivity.this);
            builder.setTitle("Wifi")
                    .setMessage("Your wifi is disable now.\nDo you want to enable it?")
                    .setPositiveButton("NOT NOW", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).setNegativeButton("ENABLE NOW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    WifiManager wifiManager = (WifiManager) Device_CategoryActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    wifiManager.setWifiEnabled(true);


                }
            }).create();
            builder.show();
        }
    }
}