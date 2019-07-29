package com.plumcookingwine.webview.remote

import android.annotation.TargetApi
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.ActionMode
import android.view.MotionEvent
import com.google.gson.Gson
import com.plumcookingwine.webview.WebSdkInit
import com.plumcookingwine.webview.bridge.JsInterface
import com.plumcookingwine.webview.callback.WebCallback
import com.plumcookingwine.webview.client.CommonWebChromeClient
import com.plumcookingwine.webview.client.CommonWebClient
import com.plumcookingwine.webview.common.WebConstants
import com.plumcookingwine.webview.remote.wrapper.CallbackWrapperBase
import com.plumcookingwine.webview.remote.wrapper.CallbackWrapperM
import com.plumcookingwine.webview.settings.CommonSettings
import com.tencent.smtt.sdk.WebView
import java.util.HashMap
import java.util.concurrent.Executors

private const val TAG = "BaseWebView"

private val THREAD_POOL = Executors.newSingleThreadExecutor()

open class BaseWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private var mTouchByUser: Boolean = false

    private var isReady: Boolean = false

    private var mWebCallBack: WebCallback? = null

    private var mCustomCallback: ActionMode.Callback? = null

    private var mHeaders: Map<String, String>? = null

    private var jsInterface: JsInterface? = null

    init {
        CommonSettings.instance.setWebSettings(this)
        this.webChromeClient = CommonWebChromeClient()
        this.webViewClient = CommonWebClient()

        if (jsInterface == null) {
            jsInterface = JsInterface(context)
            jsInterface!!.setJavascriptCommand(object : JsInterface.JavascriptCommand {
                override fun exec(context: Context, cmd: String, params: String) {
                    mWebCallBack?.exec(
                        context,
                        mWebCallBack?.getCommandLevel() ?: WebConstants.LEVEL_BASE,
                        cmd,
                        params,
                        this@BaseWebView
                    )
                }
            })
        }
        setJavascriptInterface(jsInterface!!)
    }

    override fun loadUrl(url: String?) {
        super.loadUrl(url)
        Log.e(TAG, "DWebView load url: $url")
        if (url == null) return
        resetAllStateInternal(url)
    }

    override fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {
        super.loadUrl(url, additionalHttpHeaders)
        Log.e(TAG, "DWebView load url: $url")
        resetAllStateInternal(url)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mTouchByUser = true
        }
        return super.onTouchEvent(event)
    }

    override fun startActionMode(callback: ActionMode.Callback?): ActionMode? {
        if (callback == null) return super.startActionMode(callback)
        val parent = parent
        return parent?.startActionModeForChild(this, wrapCallback(callback))
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun startActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? {
        if (callback == null) return super.startActionMode(callback, type)
        val parent = parent
        return parent?.startActionModeForChild(this, wrapCallback(callback), type)
    }


    private fun wrapCallback(callback: ActionMode.Callback): ActionMode.Callback {
        return if (mCustomCallback != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CallbackWrapperM(mCustomCallback!!, callback)
            } else {
                CallbackWrapperBase(mCustomCallback!!, callback)
            }
        } else callback
    }

    private fun resetAllStateInternal(url: String) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return
        }
        resetAllState()
    }

    private fun load(trigger: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null)
        } else {
            loadUrl(trigger)
        }
    }

    // 加载url时重置touch状态
    protected fun resetAllState() {
        mTouchByUser = false
    }

    /**
     * 开发此接口，可自定义接口设计模式
     */
    fun setJavascriptInterface(obj: JsInterface) {
        addJavascriptInterface(obj, WebSdkInit.instance.getInterfaceName())
    }

    fun registerWebCallBack(webCallBack: WebCallback) {
        this.mWebCallBack = webCallBack
    }

    fun setContent(htmlContent: String) {
        try {
            loadDataWithBaseURL(WebConstants.SCHEME.CONTENT_SCHEME, htmlContent, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun exec(trigger: String) {
        if (isReady) {
            load(trigger)
        } else {
            WaitLoad(trigger).executeOnExecutor(THREAD_POOL)
        }
    }

    fun setHeaders(mHeaders: Map<String, String>) {
        this.mHeaders = mHeaders
    }

    fun setCustomActionCallback(callback: ActionMode.Callback) {
        mCustomCallback = callback
    }

    fun isTouchByUser(): Boolean {
        return mTouchByUser
    }

    fun loadJs(cmd: String, param: Any) {
        val trigger = "javascript:$cmd(${Gson().toJson(param)})"
        evaluateJavascript(trigger, null)
    }

    fun loadJs(trigger: String) {
        if (!TextUtils.isEmpty(trigger)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                evaluateJavascript(trigger, null)
            } else {
                loadUrl(trigger)
            }
        }
    }

    fun loadJS(cmd: String, param: Any) {
        val trigger = "javascript:" + cmd + "(" + Gson().toJson(param) + ")"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null)
        } else {
            loadUrl(trigger)
        }
    }

    fun loadJS(trigger: String) {
        if (!TextUtils.isEmpty(trigger)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                evaluateJavascript(trigger, null)
            } else {
                loadUrl(trigger)
            }
        }
    }

    fun dispatchEvent(name: String) {
        val param = HashMap<String, String>(1)
        param["name"] = name
        loadJS("dj.dispatchEvent", param)
    }

    fun handleCallback(response: String) {
        if (!TextUtils.isEmpty(response)) {
            val trigger = "javascript:dj.callback($response)"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                evaluateJavascript(trigger, null)
            } else {
                loadUrl(trigger)
            }
        }
    }


    private inner class WaitLoad(private val mTrigger: String) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            while (!isReady) {
                sleep(100)

            }
            return null
        }

        override fun onPostExecute(aVoid: Void) {
            load(mTrigger)
        }

        @Synchronized
        private fun sleep(ms: Long) {
            try {
                Object().wait(ms)
            } catch (ignore: InterruptedException) {
            }

        }
    }

}