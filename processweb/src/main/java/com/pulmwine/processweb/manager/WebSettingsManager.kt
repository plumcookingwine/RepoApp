package com.pulmwine.processweb.manager

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView

class WebSettingsManager private constructor() {

    private var mWebSettings: WebSettings? = null

    companion object {

        val instance by lazy {
            WebSettingsManager()
        }

        private fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo?.isConnected ?: false
        }
    }


    /**
     * WebView 配置
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun setSettings(webView: WebView) {
        mWebSettings = webView.settings ?: throw Throwable("settings is null")
        @Suppress("DEPRECATION")
        mWebSettings!!.javaScriptEnabled = true
        mWebSettings!!.setSupportZoom(true)
        mWebSettings!!.builtInZoomControls = false
        if (isNetworkConnected(webView.context)) {
            mWebSettings!!.cacheMode = WebSettings.LOAD_DEFAULT
        } else {
            mWebSettings!!.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }

        // 硬件加速兼容性问题有点多
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

        mWebSettings!!.textZoom = 100
        mWebSettings!!.databaseEnabled = true
        mWebSettings!!.setAppCacheEnabled(true)
        mWebSettings!!.loadsImagesAutomatically = true
        mWebSettings!!.setSupportMultipleWindows(false)
        mWebSettings!!.blockNetworkImage = false//是否阻塞加载网络图片  协议http or https
        mWebSettings!!.allowFileAccess = true //允许加载本地文件html  file协议
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebSettings!!.setAllowFileAccessFromFileURLs(false) //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            mWebSettings!!.setAllowUniversalAccessFromFileURLs(false)//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        }
        mWebSettings!!.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            mWebSettings!!.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        } else {
            mWebSettings!!.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        }
        mWebSettings!!.savePassword = false
        mWebSettings!!.saveFormData = false
        mWebSettings!!.loadWithOverviewMode = true
        mWebSettings!!.useWideViewPort = true
        mWebSettings!!.domStorageEnabled = true
        mWebSettings!!.setNeedInitialFocus(true)
        mWebSettings!!.defaultTextEncodingName = "utf-8"//设置编码格式
        mWebSettings!!.defaultFontSize = 16
        mWebSettings!!.minimumFontSize = 10//设置 WebView 支持的最小字体大小，默认为 8
        mWebSettings!!.setGeolocationEnabled(true)

        val appCacheDir = webView.context.getDir("cache", Context.MODE_PRIVATE).path
        Log.i("WebSetting", "WebView cache dir: $appCacheDir")
        @Suppress("DEPRECATION")
        mWebSettings!!.databasePath = appCacheDir
        mWebSettings!!.setAppCachePath(appCacheDir)
        mWebSettings!!.setAppCacheMaxSize((1024 * 1024 * 80).toLong())

        // 用户可以自己设置useragent
        // mWebSettings!!.userAgentString = "webprogress/build you agent info"
    }

}