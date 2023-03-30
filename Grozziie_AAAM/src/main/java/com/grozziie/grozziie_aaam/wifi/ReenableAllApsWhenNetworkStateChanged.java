package com.grozziie.grozziie_aaam.wifi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import java.util.List;

public class ReenableAllApsWhenNetworkStateChanged {
    public static void schedule(final Context ctx) {
        ctx.startService(new Intent(ctx, BackgroundService.class));
    }

    private static void reenableAllAps(final Context ctx) {
        final WifiManager wifiMgr = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        if(configurations != null) {
            for(final WifiConfiguration config:configurations) {
                wifiMgr.enableNetwork(config.networkId, false);
            }
        }
    }

    public static class BackgroundService extends Service {

        private boolean mReenabled;

        private BroadcastReceiver mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                    final NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    final NetworkInfo.DetailedState detailed = networkInfo.getDetailedState();
                    if(detailed != NetworkInfo.DetailedState.DISCONNECTED
                            && detailed != NetworkInfo.DetailedState.DISCONNECTING
                            && detailed != NetworkInfo.DetailedState.SCANNING) {
                        if(!mReenabled) {
                            mReenabled = true;
                            reenableAllAps(context);
                            stopSelf();
                        }
                    }
                }
            }
        };

        private IntentFilter mIntentFilter;

        @Override
        public IBinder onBind(Intent intent) {
            return null; // We need not bind to it at all.
        }

        @Override
        public void onCreate() {
            super.onCreate();
            mReenabled = false;
            mIntentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            registerReceiver(mReceiver, mIntentFilter);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterReceiver(mReceiver);
        }

    }
}
