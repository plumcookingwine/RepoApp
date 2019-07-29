package com.plumcookingwine.webview.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import com.tencent.smtt.sdk.WebView
import com.plumcookingwine.webview.utils.NetWorkUtils
import com.tencent.smtt.sdk.WebSettings


class CommonSettings {


    companion object {

        val instance by lazy { CommonSettings() }
    }


    @SuppressLint("SetJavaScriptEnabled")
    fun setWebSettings(webView: WebView) {

        val settings = webView.settings
//            x5浏览器禁止调用 https://x5.tencent.com/tbs/guide/sdkInit.html
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//
//            }
        @Suppress("DEPRECATION")
        settings.javaScriptEnabled = true//启用js
        settings.javaScriptCanOpenWindowsAutomatically = true//js和android交互
        settings.allowFileAccess = true // 允许访问文件
        settings.saveFormData = false
        settings.useWideViewPort = true//设置webview自适应屏幕大小
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS//设置，可能的话使所有列的宽度不超过屏幕宽度
        settings.loadWithOverviewMode = true//设置webview自适应屏幕大小
        settings.builtInZoomControls = false//关闭zoom
        settings.minimumFontSize = 10 // 设置最小字体大小
        settings.defaultFontSize= 16 // 默认字体
        settings.defaultTextEncodingName = "utf-8" // 编码格式
        settings.domStorageEnabled = true//设置可以使用localStorage
        if (NetWorkUtils.isNetworkConnected(webView.context)) {
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK;
        }
        settings.displayZoomControls = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.textSize = WebSettings.TextSize.NORMAL
        }
        try {
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val cacheDir = webView.context.getDir("cache", Context.MODE_PRIVATE).path
        @Suppress("DEPRECATION")
        settings.databasePath = cacheDir
        settings.setAppCacheMaxSize(1024 * 1024 * 80)
        settings.setAppCachePath(cacheDir)
        settings.setAppCacheEnabled(true) //设置H5的缓存打开,默认关闭
        settings.setSupportZoom(false)//关闭zoom按钮
        settings.setGeolocationEnabled(true)
        settings.setNeedInitialFocus(true)
    }
}