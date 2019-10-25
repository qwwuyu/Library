package com.qwwuyu.lib.helper;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;

import com.qwwuyu.lib.utils.LogUtils;
import com.qwwuyu.lib.utils.NetUtils;


public class NetworkBroadcastReceiver extends BroadcastReceiver {
    public static void registerNetworkListener(Application application) {
        application.registerReceiver(new NetworkBroadcastReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private static final int WHAT_NETWORK_CHANGE = 65535;
    private long initTime;
    private boolean isFirstTime = true;
    private Handler handler = new Handler(msg -> {
        if (WHAT_NETWORK_CHANGE == msg.what) {
            LogUtils.i("NetworkBroadcastReceiver", "Network Handler:" + msg.obj);
            onNetWorkChange((Boolean) msg.obj);
            return true;
        }
        return false;
    });

    private NetworkBroadcastReceiver() {
        initTime = System.currentTimeMillis();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                if (isFirstTime && System.currentTimeMillis() - initTime < 1000) {
                    isFirstTime = false;
                    return;
                }
                boolean available = NetUtils.isNetworkAvailable();
                LogUtils.i("NetworkBroadcastReceiver", "onReceive:" + available);
                handler.removeMessages(WHAT_NETWORK_CHANGE);
                handler.sendMessageDelayed(handler.obtainMessage(WHAT_NETWORK_CHANGE, available), 1000);
            }
        }
    }

    private void onNetWorkChange(boolean available) {
        if (available) {
            RxBus.getDefault().post(new Object());
        } else {
            RxBus.getDefault().post(new Object());
        }
    }

    /*public class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback {

        final NetworkRequest networkRequest;

        public ConnectionStateMonitor() {
            networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();
        }

        public void enable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerNetworkCallback(networkRequest, this);
        }

        // Likewise, you can have a disable method that simply calls ConnectivityManager.unregisterNetworkCallback(NetworkCallback) too.

        @Override
        public void onAvailable(Network network) {
        }

        @Override
        public void onUnavailable() {
        }
    }*/
}
