package com.grozziie.grozziie_aaam.wifi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grozziie.grozziie_aaam.R;
import com.grozziie.grozziie_aaam.WifiPrinterCategory;
import com.tbruyelle.rxpermissions2.RxPermissions;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.content.ContentValues.TAG;
import static com.grozziie.grozziie_aaam.wifi.Constants.TIME_REFRESH_WIFI_INTERVAL;
import static com.grozziie.grozziie_aaam.wifi.Constants.TIME_SCAN_DELAY_LONG;
import static com.grozziie.grozziie_aaam.wifi.Constants.TIME_SCAN_DELAY_NORMAL;


public class WifiListActivity extends FragmentActivity implements AdapterView.OnItemClickListener, InputWifiPwdFragment.WifiPwdInputListener {

    private long mScanTimeDelay = TIME_SCAN_DELAY_NORMAL;
    private Disposable mScanWifiDisposable = null;
    private WifiManager mWifiManager = null;
    private RxPermissions mRxPermissions = null;
    private List<ScanResult> mWifiList = null;
    private ListView mListView = null;
    private SwipeRefreshLayout mRefreshView = null;
    private WifiListAdapter mAdapter = null;
    private DialogFragment mPwdFragment = null;
    private BroadcastReceiver mScanWifiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                getWifiList();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);
        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            toolbar.setTitle("Wifi List");
        }
        else {
            toolbar.setTitle("Wifi 列表");
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);




        mRefreshView = findViewById(R.id.swipe);
        mListView = findViewById(R.id.listView);
        mRxPermissions = new RxPermissions(this);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mRefreshView.setRefreshing(true);
        findViewById(R.id.top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (mWifiManager.isWifiEnabled()) {
            mRxPermissions.request(Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE).subscribe(granted -> {
                if (granted) {
                    mWifiManager.startScan();
                    getWifiList();
                }
            });
        }
    }

    protected void getWifiList() {
        mRxPermissions.request(ACCESS_WIFI_STATE, CHANGE_WIFI_STATE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION).subscribe(granted -> {
            if (granted) {
                if (PhoneUtils.isGpsEnable(WifiListActivity.this)) {
                    Observable.timer(mScanTimeDelay, TimeUnit.MILLISECONDS).observeOn(
                            AndroidSchedulers.mainThread()
                    ).subscribe(aLong ->
                            showWifiList(mWifiManager.getScanResults())
                    );
                } else {
                    ToastUtil.show(R.string.open_location_service);
                    PhoneUtils.openLocationSetting(this);
                    finish();
                }
            } else {
                grantPermissionFailed();
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mWifiManager.isWifiEnabled()) {
            boolean result = mWifiManager.setWifiEnabled(true);
            if (result) {
                mScanTimeDelay = TIME_SCAN_DELAY_LONG;
            } else {
                Toast.makeText(WifiListActivity.this, "Please go to the system WIFI list for manual WIFI connection", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } else {
            mScanTimeDelay = TIME_SCAN_DELAY_NORMAL;
        }

        final IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mScanWifiReceiver, filter);
        if (mScanWifiDisposable != null && !mScanWifiDisposable.isDisposed()) {
            mScanWifiDisposable.isDisposed();
        }

        scanWifi();


    }

    void scanWifi() {
        mRxPermissions.request(Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE).subscribe(granted -> {
            if (granted) {
                mWifiManager.startScan();
            } else {
                ToastUtil.show(R.string.grant_net_permission);
            }
        });
    }

    protected void grantPermissionFailed() {
        ToastUtil.show(R.string.ungot_location_service);
        mRefreshView.setRefreshing(false);
    }


    public void showWifiList(List<ScanResult> scanResultList) {

        if (!scanResultList.isEmpty()) {
            WifiUtils.filterWifi(scanResultList);
            mWifiList = scanResultList;
            mAdapter = new WifiListAdapter(scanResultList);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(this);

        }
        Observable.timer(TIME_REFRESH_WIFI_INTERVAL, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                mScanWifiDisposable = d;
            }

            @Override
            public void onNext(Long aLong) {
                mWifiManager.startScan();
            }

            @Override
            public void onError(Throwable e) {
                mRefreshView.setRefreshing(false);
                DisposableUtil.dispose(mScanWifiDisposable);
            }

            @Override
            public void onComplete() {
                mRefreshView.setRefreshing(false);
                DisposableUtil.dispose(mScanWifiDisposable);
            }
        });
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScanResult scan = mWifiList.get(position);

        if (WifiUtils.isCurrentWifi(this, scan.SSID)) {
            final Dialog mDialog = new Dialog(WifiListActivity.this);


            //mDialog = new Dialog(HomeACTIVITY.this);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //LayoutInflater factory = LayoutInflater.from(this);

            mDialog.setContentView(R.layout.my_new_layout);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            FloatingActionButton dialogClose=(FloatingActionButton)mDialog.findViewById(R.id.dialogClose);
            TextView name=(TextView)mDialog.findViewById(R.id.name);
            name.setText(""+scan.SSID);
            EditText pwd=(EditText)mDialog.findViewById(R.id.pwd);
            TextView confirm=(TextView)mDialog.findViewById(R.id.confirm);
            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                pwd.setHint("Please enter password");
                confirm.setText("Print Now");
            }
            else {
                pwd.setHint("请输入密码");
                confirm.setText("立即打印");
            }

            dialogClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            pwd.setText(scan.BSSID);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password=pwd.getText().toString();
                    if (TextUtils.isEmpty(password)) {
                        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                        if (defaultlanguage.toLowerCase().toString().equals("english")) {
                            Toast.makeText(WifiListActivity.this, "Enter  IP address ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(WifiListActivity.this, "输入IP地址", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                       Intent intent=new Intent(getApplicationContext(), WifiPrinterCategory.class);
                       intent.putExtra("wifi_name",name.getText().toString());
                        intent.putExtra("wifi_ipaddress",pwd.getText().toString());
                       /// Toast.makeText(WifiListActivity.this, name.getText().toString()+""+pwd.getText().toString(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }


                }
            });
            mDialog.show();
         /*
            new AlertDialog.Builder(this).setMessage(R.string.search_on_current_wifi).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create().show();
          */

        } else {
            ///  Toast.makeText(this, "dddd", Toast.LENGTH_SHORT).show();
            ///wifiOperate(scan);
            String wifiname = scan.SSID;
            String wifipassword = scan.BSSID;

            try {

                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                List<ScanResult> networkList = wifi.getScanResults();

//get current connected SSID for comparison to ScanResult
                WifiInfo wi = wifi.getConnectionInfo();

                String currentSSID = scan.SSID;
                ///Toast.makeText(this, ""+currentSSID, Toast.LENGTH_SHORT).show();
                if (networkList != null) {
                    for (ScanResult network : networkList) {
                        //check if current connected SSID

                        if (currentSSID.equals(network.SSID)) {

                            //get capabilities of current connection
                            String capabilities =  network.capabilities;
                            Log.d(TAG, network.SSID + " capabilities : " + capabilities);
                           /// Toast.makeText(this, network.SSID+""+currentSSID+" "+capabilities, Toast.LENGTH_SHORT).show();
                            if (capabilities.contains("WPA2")) {
                                final Dialog mDialog = new Dialog(WifiListActivity.this);


                                //mDialog = new Dialog(HomeACTIVITY.this);
                                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                //LayoutInflater factory = LayoutInflater.from(this);

                                mDialog.setContentView(R.layout.new_layout);
                                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                FloatingActionButton dialogClose=(FloatingActionButton)mDialog.findViewById(R.id.dialogClose);
                                TextView name=(TextView)mDialog.findViewById(R.id.name);
                                name.setText(""+network.SSID);
                                EditText pwd=(EditText)mDialog.findViewById(R.id.pwd);
                                TextView confirm=(TextView)mDialog.findViewById(R.id.confirm);
                                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                                if (defaultlanguage.toLowerCase().toString().equals("english")) {
                                    pwd.setHint("Please enter password");
                                    confirm.setText("Confirm");
                                }
                                else {
                                    pwd.setHint("请输入密码");
                                    confirm.setText("确认");
                                }

                                dialogClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                    }
                                });

                                confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String password=pwd.getText().toString();
                                        if (TextUtils.isEmpty(password)) {
                                            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                                            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                                                Toast.makeText(WifiListActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(WifiListActivity.this, "输入密码", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        else {
                                            ConnectToNetworkWPA(network.SSID,password);
                                            boolean status= ConnectToNetworkWPA(network.SSID,password);
                                            Toast.makeText(WifiListActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                mDialog.show();


                                //do something
                            } else if (capabilities.contains("WPA")) {
                                final Dialog mDialog = new Dialog(WifiListActivity.this);


                                //mDialog = new Dialog(HomeACTIVITY.this);
                                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                //LayoutInflater factory = LayoutInflater.from(this);

                                mDialog.setContentView(R.layout.new_layout);
                                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                FloatingActionButton dialogClose=(FloatingActionButton)mDialog.findViewById(R.id.dialogClose);
                                TextView name=(TextView)mDialog.findViewById(R.id.name);
                                name.setText(""+network.SSID);
                                EditText pwd=(EditText)mDialog.findViewById(R.id.pwd);
                                TextView confirm=(TextView)mDialog.findViewById(R.id.confirm);
                                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                                if (defaultlanguage.toLowerCase().toString().equals("english")) {
                                    pwd.setHint("Please enter password");
                                    confirm.setText("Confirm");
                                }
                                else {
                                    pwd.setHint("请输入密码");
                                    confirm.setText("确认");
                                }

                                dialogClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                    }
                                });

                                confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String password=pwd.getText().toString();
                                        if (TextUtils.isEmpty(password)) {
                                            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                                            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                                                Toast.makeText(WifiListActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(WifiListActivity.this, "输入密码", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            ConnectToNetworkWPA(network.SSID,password);
                                            boolean status= ConnectToNetworkWPA(network.SSID,password);
                                            Toast.makeText(WifiListActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                mDialog.show();
                                //do something
                            } else if (capabilities.contains("WEP")) {
                                //do something
                                final Dialog mDialog = new Dialog(WifiListActivity.this);


                                //mDialog = new Dialog(HomeACTIVITY.this);
                                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                //LayoutInflater factory = LayoutInflater.from(this);

                                mDialog.setContentView(R.layout.new_layout);
                                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                FloatingActionButton dialogClose=(FloatingActionButton)mDialog.findViewById(R.id.dialogClose);
                                TextView name=(TextView)mDialog.findViewById(R.id.name);
                                name.setText(""+network.SSID);
                                EditText pwd=(EditText)mDialog.findViewById(R.id.pwd);
                                TextView confirm=(TextView)mDialog.findViewById(R.id.confirm);
                                String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                                if (defaultlanguage.toLowerCase().toString().equals("english")) {
                                    pwd.setHint("Please enter password");
                                    confirm.setText("Confirm");
                                }
                                else {
                                    pwd.setHint("请输入密码");
                                    confirm.setText("确认");
                                }


                                dialogClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                    }
                                });
                                //TextView confirm=(TextView)mDialog.findViewById(R.id.confirm);
                                confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String password=pwd.getText().toString();
                                        if (TextUtils.isEmpty(password)) {
                                            String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
                                            if (defaultlanguage.toLowerCase().toString().equals("english")) {
                                                Toast.makeText(WifiListActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(WifiListActivity.this, "输入密码", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            ConnectToNetworkWEP(network.SSID,password);
                                            boolean status= ConnectToNetworkWEP(network.SSID,password);
                                            Toast.makeText(WifiListActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                mDialog.show();
                            }
                        }
                    }
                }
            }catch (Exception e) {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }

    }
    public boolean ConnectToNetworkWEP( String networkSSID, String password )
    {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain SSID in quotes
            conf.wepKeys[0] = "\"" + password + "\""; //Try it with quotes first

            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.SHARED);


            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);

            if (networkId == -1){
                //Try it again with no quotes in case of hex password
                conf.wepKeys[0] = password;
                networkId = wifiManager.addNetwork(conf);
            }

            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    break;
                }
            }

            //WiFi Connection success, return true
            return true;
        } catch (Exception ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return false;
        }
    }
    public boolean ConnectToNetworkWPA( String networkSSID, String password )
    {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain SSID in quotes

            conf.preSharedKey = "\"" + password + "\"";

            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            Log.d("connecting", conf.SSID + " " + conf.preSharedKey);

            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.addNetwork(conf);

            Log.d("after connecting", conf.SSID + " " + conf.preSharedKey);



            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    Log.d("re connecting", i.SSID + " " + conf.preSharedKey);

                    break;
                }
            }


            //WiFi Connection success, return true
            return true;
        } catch (Exception ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return false;
        }
    }
    private String getEncryptionType(WifiConfiguration config) {
        String encryptionType = "";
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            encryptionType = "WPA2";
        } else if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE)) {
            encryptionType = "NONE";
        } else {
            encryptionType = "WEP";
        }
        return encryptionType;
    }
    protected void wifiOperate(final ScanResult scanResult) {

        if (scanResult == null) {
            ToastUtil.show(R.string.invalid_wifi);
            return;
        }
        if (WifiUtils.isAdHoc(scanResult)) {
            ToastUtil.show(R.string.adhoc_not_supported_yet);
            finish();
            return;
        }
        final String security = Wifi.ConfigSec.getScanResultSecurity(scanResult);
        final WifiConfiguration config = Wifi.getWifiConfiguration(mWifiManager, scanResult, security);
        if (config == null) {
            //
            mPwdFragment = InputWifiPwdFragment.show(this, scanResult);
        } else {
            //
            WifiConnector connector = new WifiConnector(this, scanResult);
            if (connector.connect()) {
            } else {
                ToastUtil.show(R.string.please_go_system_wifi_setting);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mScanWifiReceiver);
    }


    @Override
    public void onWifiPwdInputCallback(ScanResult result, String pwd) {

        WifiConnector connector = new WifiConnector(this, result);
        connector.connectNewWifi(pwd);
        mPwdFragment.dismiss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisposableUtil.dispose(mScanWifiDisposable);
    }
}