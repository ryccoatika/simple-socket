package com.ryccoatika.socketserver.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import androidx.annotation.RequiresApi
import java.net.Inet4Address

abstract class NetworkUtils {
    companion object {
        fun getLocalIpv4Address(context: Context): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                getLocalIpAddressSAbove(context)
            } else {
                getLocalIpAddressSBelow(context)
            }
        }

        @Suppress("DEPRECATION")
        private fun getLocalIpAddressSBelow(context: Context): String {
            val wifiManager = context
                .applicationContext
                .getSystemService(Context.WIFI_SERVICE) as? WifiManager? ?: return ""
            val connectionInfo = wifiManager.connectionInfo
            return Formatter.formatIpAddress(connectionInfo.ipAddress)
        }

        @RequiresApi(Build.VERSION_CODES.S)
        private fun getLocalIpAddressSAbove(context: Context): String {
            val connectivityManager =
                context
                    .applicationContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?
                    ?: return ""
            val activeNetwork = connectivityManager.activeNetwork ?: return ""
            val linkProperties = connectivityManager.getLinkProperties(activeNetwork) ?: return ""
            val ipv4Addresses = linkProperties.linkAddresses.filter { it.address is Inet4Address }
            val linkAddress = ipv4Addresses.firstOrNull() ?: return ""
            return linkAddress.address.hostAddress ?: ""
        }
    }
}