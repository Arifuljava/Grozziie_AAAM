package com.grozziie.grozziie_aaam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grozziie.grozziie_aaam.print_bluetooth.BluetoothImagePrinting;
import com.psp.bluetoothlibrary.Bluetooth;
import com.psp.bluetoothlibrary.BluetoothListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class BluetoothList extends AppCompatActivity {
    private static final String TAG = "psp.BluetoothAct";

    // UI
    Button btnTurnOn,btnTurnOff,btnScan;
    ListView listViewPairedDevices, listViewDetectDevices;

    // List For paired devices and detect devices
    ArrayList<String> listDetectDevicesString, listPairedDevicesString;
    ArrayList<BluetoothDevice> listDetectBluetoothDevices,listPairedBluetoothDevices;
    ArrayAdapter<String> adapterDetectBluetoothDevices,adapterPairedBluetoothDevices;

    // Bluetooth object
    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);
        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            toolbar.setTitle("Bluetooth Devices");
        }
        else {
            toolbar.setTitle("蓝牙设备");
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        init();

        // Request fine location permission
        checkRunTimePermission();

        /*
        <uses-permission android:name="android.permission.BLUETOOTH" />
        <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

        - add this in your manifests file
         */

        // initialize bluetooth
        bluetooth = new Bluetooth(this);

        // check bluetooth is supported or not
        Log.d(TAG,"Bluetooth is supported "+Bluetooth.isBluetoothSupported());
        Log.d(TAG,"Bluetooth is on "+bluetooth.isOn());
        Log.d(TAG,"Bluetooth is discovering "+bluetooth.isDiscovering());

        // turn on bluetooth
        btnTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // With user permission

                // Without user permission
                // bluetooth.turnOnWithoutPermission();
                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                if (defaultlanguage.toLowerCase().toString().equals("english")) {

                    AlertDialog.Builder builder=new AlertDialog.Builder(BluetoothList.this);
                    builder.setTitle("Confirmation")
                            .setMessage("Do you want to enable this bluetooth?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bluetooth.turnOnWithPermission(BluetoothList.this);
                        }
                    }).create();
                    builder.show();
                }
                else {

                    AlertDialog.Builder builder=new AlertDialog.Builder(BluetoothList.this);
                    builder.setTitle("确认")
                            .setMessage("您要启用此蓝牙吗？")
                            .setPositiveButton("不", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bluetooth.turnOnWithPermission(BluetoothList.this);
                        }
                    }).create();
                    builder.show();
                }


            }
        });

        // turn off bluetooth
        btnTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                if (defaultlanguage.toLowerCase().toString().equals("english")) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(BluetoothList.this);
                    builder.setTitle("Confirmation")
                            .setMessage("Do you want to enable this bluetooth?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bluetooth.turnOff(); // turn off
                        }
                    }).create();
                    builder.show();
                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(BluetoothList.this);
                    builder.setTitle("确认")
                            .setMessage("您要启用此蓝牙吗？")
                            .setPositiveButton("不", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bluetooth.turnOff(); // turn off
                        }
                    }).create();
                    builder.show();
                }



            }
        });


        // Bluetooth discovery #START
        bluetooth.setOnDiscoveryStateChangedListener(new BluetoothListener.onDiscoveryStateChangedListener() {
            @Override
            public void onDiscoveryStateChanged(int state) {
                if(state == Bluetooth.DISCOVERY_STARTED) {
                    Log.d(TAG,"Discovery started");
                }

                if(state == Bluetooth.DISCOVERY_FINISHED) {
                    Log.d(TAG,"Discovery finished");
                }
            }
        });
        // Bluetooth discovery #END

        // Detect nearby bluetooth devices #START
        bluetooth.setOnDetectNearbyDeviceListener(new BluetoothListener.onDetectNearbyDeviceListener() {
            @Override
            public void onDeviceDetected(BluetoothDevice device) {
                // check device is already in list or not
                if(!listDetectDevicesString.contains(device.getName())) {
                    Log.d(TAG, "Bluetooth device found " + device.getName());
                    listDetectDevicesString.add(device.getName()); // add to list
                    listDetectBluetoothDevices.add(device);
                    adapterDetectBluetoothDevices.notifyDataSetChanged();
                }
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear all devices list
                clearDetectDeviceList();
                // scan nearby bluetooth devices
                bluetooth.startDetectNearbyDevices();
            }
        });
        // Detect nearby bluetooth devices #END

        // Bluetooth Pairing #START
        bluetooth.setOnDevicePairListener(new BluetoothListener.onDevicePairListener() {
            @Override
            public void onDevicePaired(BluetoothDevice device) {
                Log.d(TAG,device.getName()+" Paired successfull");
                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                if (defaultlanguage.toLowerCase().toString().equals("english")) {
                    Toast.makeText(BluetoothList.this, device.getName()+" Paired successfull", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(BluetoothList.this, device.getName()+" 配对成功", Toast.LENGTH_SHORT).show();
                }



                // remove device from detect device list
                listDetectDevicesString.remove(device.getName());
                listDetectBluetoothDevices.remove(device);
                adapterDetectBluetoothDevices.notifyDataSetChanged();

                // add device to paired device list
                listPairedDevicesString.add(device.getName());
                listPairedBluetoothDevices.add(device);
                adapterPairedBluetoothDevices.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(BluetoothDevice device) {
                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                if (defaultlanguage.toLowerCase().toString().equals("english")) {
                    Toast.makeText(BluetoothList.this, device.getName()+" Paired failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(BluetoothList.this, device.getName()+"配对失败", Toast.LENGTH_SHORT).show();
                }


            }
        });

        listViewDetectDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(bluetooth.requestPairDevice(listDetectBluetoothDevices.get(position))) {
                    Log.d(TAG,"Pair request send successfully");
                    String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                    if (defaultlanguage.toLowerCase().toString().equals("english")) {
                        Toast.makeText(BluetoothList.this, "Pair request send successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(BluetoothList.this, "配对请求发送成功", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        // Bluetooth Pairing #END


        // Get Paired devices list
        getPairedDevices();


        // Unpair bluetooh device #START
        listViewPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(bluetooth.unpairDevice(listPairedBluetoothDevices.get(position))) {
                    Log.d(TAG,"Unpair successfully");

                   // listPairedDevicesString.remove(position);
                    //listPairedBluetoothDevices.remove(position);
                   /// adapterPairedBluetoothDevices.notifyDataSetChanged();
                    String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                    if (defaultlanguage.toLowerCase().toString().equals("english")) {
                        String mac=listPairedBluetoothDevices.get(position).getAddress();
                        String mesge="Device Name : "+listPairedBluetoothDevices.get(position).getName()+"\n" +
                                "Device MAC Address : "+listPairedBluetoothDevices.get(position).getAddress();
                        AlertDialog.Builder builder=new AlertDialog.Builder(BluetoothList.this);
                        builder.setTitle("Confirmation")
                                .setMessage("Printer Details\n"+mesge+"\n\nDo you want to set this printer as your printer?")
                                .setPositiveButton("NOT NOW", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("YES CONTINUE NOW", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            if (listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-ece5")||
                                    listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-5502")||
                                    listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-0022")||
                                    listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-ece5")||
                                    listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-ece5")||listPairedBluetoothDevices.get(position).getName().toLowerCase().equals("ac695x_1")||listDetectBluetoothDevices.get(position).getName().toLowerCase().equals("gd610")||
                            listDetectBluetoothDevices.get(position).getName().toLowerCase().equals("gd620")||listDetectBluetoothDevices.get(position).getName().toLowerCase().equals("gzp660")
                            || listDetectBluetoothDevices.get(position).getName().toLowerCase().equals("tjyd600")|| listDetectBluetoothDevices.get(position)
                            .getName().toLowerCase().equals("tjyd610")||listDetectBluetoothDevices.get(position).
                                    getName().toLowerCase().equals("tjyd620")) {
                                startActivity(new Intent(getApplicationContext(), PrintBluetoothcategory.class));




                                return;
                            }
                            else {
                                startActivity(new Intent(getApplicationContext(), PrintBluetoothcategory.class));
                            }
                            }
                        }).create();
                        builder.show();
                    }
                    else {
                        String mesge="设备名称 : "+listPairedBluetoothDevices.get(position).getName()+"\n" +
                                "设备MAC地址 : "+listPairedBluetoothDevices.get(position).getAddress();
                        AlertDialog.Builder builder=new AlertDialog.Builder(BluetoothList.this);
                        builder.setTitle("确认")
                                .setMessage("Printer Details\n"+mesge+"\n\nDo you want to set this printer as your printer?")
                                .setPositiveButton("现在不要", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("是 现在连接", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-ece5")||
                                        listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-5502")||
                                        listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-0022")||
                                        listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-ece5")||
                                        listDetectBluetoothDevices.get(position).getName().toLowerCase().contains("gd610-ece5")||listPairedBluetoothDevices.get(position).getName().toLowerCase().equals("ac695x_1")||listDetectBluetoothDevices.get(position).getName().toLowerCase().equals("gd610")||
                                        listDetectBluetoothDevices.get(position).getName().toLowerCase().equals("gd620")||listDetectBluetoothDevices.get(position).getName().toLowerCase().equals("gzp660")
                                        || listDetectBluetoothDevices.get(position).getName().toLowerCase().equals("tjyd600")|| listDetectBluetoothDevices.get(position)
                                        .getName().toLowerCase().equals("tjyd610")||listDetectBluetoothDevices.get(position).
                                        getName().toLowerCase().equals("tjyd620")) {
                                    startActivity(new Intent(getApplicationContext(), PrintBluetoothcategory.class));




                                    return;
                                }
                                else {
                                    startActivity(new Intent(getApplicationContext(), PrintBluetoothcategory.class));
                                }
                            }
                        }).create();
                        builder.show();
                    }



                }
                else {
                    Log.d(TAG,"Unpair failed");
                }
            }
        });
        // Unpair bluetooh device #END


    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.onStop();
        Log.d(TAG,"OnStop");
    }

    private void init() {
        btnTurnOn = findViewById(R.id.btnTurnOn);
        btnTurnOff = findViewById(R.id.btnTurnOff);
        btnScan = findViewById(R.id.btnScan);
        listViewPairedDevices = findViewById(R.id.listViewPairedDevice);
        listViewDetectDevices = findViewById(R.id.listViewDetectDevice);

        listDetectDevicesString = new ArrayList<>();
        listPairedDevicesString = new ArrayList<>();

        listDetectBluetoothDevices = new ArrayList<>();
        listPairedBluetoothDevices = new ArrayList<>();

        adapterDetectBluetoothDevices = new ArrayAdapter<String>(this, R.layout.device_item, listDetectDevicesString);
        adapterPairedBluetoothDevices = new ArrayAdapter<>(this,R.layout.device_item, listPairedDevicesString);

        listViewDetectDevices.setAdapter(adapterDetectBluetoothDevices);
        listViewPairedDevices.setAdapter(adapterPairedBluetoothDevices);
    }

    private void getPairedDevices() {
        ArrayList<BluetoothDevice> devices = bluetooth.getPairedDevices();

        if(devices.size() > 0) {
            for(BluetoothDevice device: devices) {
                listPairedDevicesString.add(device.getName());
                listPairedBluetoothDevices.add(device);
                Log.d(TAG,"Paired device is "+device.getName());
            }
        }
        else {
            Log.d(TAG,"Paired device list not found");
        }
    }

    public void checkRunTimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"Fine location permission is already granted");
            } else {
                Log.d(TAG,"request fine location permission");
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        10);
            }
        }
    }

    private void clearDetectDeviceList() {
       /// Toast.makeText(BluetoothList.this, "ffff", Toast.LENGTH_SHORT).show();
        if(listDetectDevicesString.size() > 0) {
            listDetectDevicesString.clear();
        }

        if(listDetectBluetoothDevices.size() > 0) {
            listDetectBluetoothDevices.clear();
        }
        adapterDetectBluetoothDevices.notifyDataSetChanged();
    }

    // optional
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Bluetooth.BLUETOOTH_ENABLE_REQUEST) {
            if(resultCode == RESULT_OK) {
                Log.d(TAG,"Bluetooth on");
            }

            if(resultCode == RESULT_CANCELED) {
                Log.d(TAG,"Bluetooth turn on dialog canceled");
            }
        }
    }

}