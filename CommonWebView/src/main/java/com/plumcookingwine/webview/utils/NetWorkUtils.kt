package com.plumcookingwine.webview.utils

import android.content.Context
import android.net.ConnectivityManager

class NetWorkUtils {

    companion object {

        fun isNetworkConnected(context: Context?): Boolean {
            if (context != null) {
                val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val mNetworkInfo = mConnectivityManager.activeNetworkInfo
                if (mNetworkInfo != null) {
                    @Suppress("UsePropertyAccessSyntax", "DEPRECATION")
                    return mNetworkInfo.isAvailable()
                }
            }
            return false
        }
    }
}