package com.ryccoatika.socketserver.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;

import androidx.annotation.RequiresApi;

import java.net.Inet4Address;
import java.util.List;

public abstract class NetworkUtils {
    static public String getLocalIpv4Address(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getLocalIpAddressSAbove(context);
        } else {
            return getLocalIpAddressSBelow(context);
        }
    }

    /**
     * @noinspection deprecation
     */
    static private String getLocalIpAddressSBelow(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return Formatter.formatIpAddress(wifiInfo.getIpAddress());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    static private String getLocalIpAddressSAbove(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
        assert linkProperties != null;
        List<LinkAddress> linkAddresses = linkProperties.getLinkAddresses();
        for (LinkAddress linkAddress : linkAddresses) {
            if (linkAddress.getAddress() instanceof Inet4Address) {
                return linkAddress.getAddress().getHostAddress();
            }
        }
        return "";
    }
}
