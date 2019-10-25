package com.qwwuyu.lib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by qiwei on 2018/5/4 14:48
 * Description wifi工具.
 */
public class WifiUtils {
    public static int getLevel(ScanResult result) {
        if (result.level > -65) {
            return 0;
        } else if (result.level > -75) {
            return 1;
        } else if (result.level > -85) {
            return 2;
        } else {
            return 3;
        }
    }

    /** 是否是2.4G网络 */
    public static boolean isAvailableWifi(ScanResult result) {
        return result.frequency >= 2400 && result.frequency <= 2500;
    }

    /** 获取对应的加密类型 */
    public static String getType(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return "WEP";
        } else if (result.capabilities.contains("WPA")) {
            return "WPA";
        } else if (result.capabilities.contains("EAP")) {
            return "OTHER";
        }
        return "NONE";
    }

    public static String getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            if (config.allowedProtocols.get(WifiConfiguration.Protocol.RSN)) {
                return "WPA";
            } else {
                return "WPA";
            }
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X))
            return "OTHER";
        return (config.wepKeys[0] != null) ? "WEP" : "NONE";
    }

    /** 判断定位是否打开 */
    public static boolean isLocationEnable(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    int LOCATION_MODE = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
                    if (LOCATION_MODE != Settings.Secure.LOCATION_MODE_OFF) return true;
                } catch (Settings.SettingNotFoundException e) {
                    //另外一种
                    LocationManager lm = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                    try {
                        if (lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
                            return true;
                    } catch (Exception ignored) {
                    }
                    try {
                        if (lm != null && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                            return true;
                    } catch (Exception ignored) {
                    }
                    try {
                        if (lm != null && lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
                            return true;
                    } catch (Exception ignored) {
                    }
                }
            } else {
                return !TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED));
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public static void goWifiSetting(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void goLocationSetting(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * need {@link android.Manifest.permission#ACCESS_WIFI_STATE}{@link android.Manifest.permission#CHANGE_WIFI_STATE}
     */
    @SuppressLint("MissingPermission")
    public static void connectWifi(WifiManager wifiManager, String wifiType, String ssid, String pwd) {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
        if ("WPA".equals(wifiType)) {
            conf.preSharedKey = "\"" + pwd + "\"";
        } else if ("WEP".equals(wifiType)) {
            conf.wepKeys[0] = "\"" + pwd + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        wifiManager.addNetwork(conf);
        List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        if (configurations == null) return;
        for (WifiConfiguration i : configurations) {
            if (("\"" + ssid + "\"").equals(i.SSID) || ssid.equals(i.SSID)) {
                wifiManager.enableNetwork(i.networkId, true);
                break;
            }
        }
    }
}
