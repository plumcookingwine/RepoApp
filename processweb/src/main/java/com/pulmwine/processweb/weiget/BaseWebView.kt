package com.pulmwine.processweb.weiget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.net.http.SslError
import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.pulmwine.processweb.R
import com.pulmwine.processweb.WebInitSdk
import com.pulmwine.processweb.bridge.WebJsInterface
import com.pulmwine.processweb.callback.WebViewCallBack
import com.pulmwine.processweb.client.BaseWebChromeClient
import com.pulmwine.processweb.manager.WebSettingsManager
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import java.util.*

import java.util.concurrent.Executors

/**
 * Created by xud on 2018/9/1.
 */
class BaseWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private var mCustomCallback: ActionMode.Callback? = null

    var isReady: Boolean = false
        internal set

    private var mWebViewCallBack: WebViewCallBack? = null

    private var mHeaders: Map<String, String>? = null

    private var remoteInterface: WebJsInterface? = null

    var isTouchByUser: Boolean = false
        private set


    fun registerWebViewCallBack(webViewCallBack: WebViewCallBack) {
        this.mWebViewCallBack = webViewCallBack
    }

    fun setHeaders(mHeaders: Map<String, String>) {
        this.mHeaders = mHeaders
    }

    init {
        WebSettingsManager.instance.setSettings(this)
        webViewClient = DWebViewClient()
        webChromeClient = BaseWebChromeClient()

        /**
         * Web Native交互触发
         */
        if (remoteInterface == null) {
            remoteInterface = WebJsInterface(context)
            remoteInterface!!.setAidlCommand(object : WebJsInterface.AidlCommand {
                override fun exec(context: Context, cmd: String, param: String) {
                    if (mWebViewCallBack != null) {
                        mWebViewCallBack!!.exec(
                            context,
                            mWebViewCallBack!!.commandLevel(),
                            cmd,
                            param,
                            this@BaseWebView
                        )
                    }
                }
            })
        }
        setJavascriptInterface(remoteInterface!!)
    }

    override fun startActionMode(callback: ActionMode.Callback): ActionMode? {
        val parent = parent
        return parent?.startActionModeForChild(this, wrapCallback(callback))
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun startActionMode(callback: ActionMode.Callback, type: Int): ActionMode? {
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

    fun setCustomActionCallback(callback: ActionMode.Callback) {
        mCustomCallback = callback
    }

    private class CallbackWrapperBase(
        private val mWrappedCustomCallback: ActionMode.Callback,
        private val mWrappedSystemCallback: ActionMode.Callback
    ) : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            return mWrappedCustomCallback.onCreateActionMode(mode, menu)
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return mWrappedCustomCallback.onPrepareActionMode(mode, menu)
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return mWrappedCustomCallback.onActionItemClicked(mode, item)
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            try {
                mWrappedCustomCallback.onDestroyActionMode(mode)
                mWrappedSystemCallback.onDestroyActionMode(mode)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private class CallbackWrapperM(
        private val mWrappedCustomCallback: ActionMode.Callback,
        private val mWrappedSystemCallback: ActionMode.Callback
    ) : ActionMode.Callback2() {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            return mWrappedCustomCallback.onCreateActionMode(mode, menu)
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return mWrappedCustomCallback.onPrepareActionMode(mode, menu)
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return mWrappedCustomCallback.onActionItemClicked(mode, item)
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            mWrappedCustomCallback.onDestroyActionMode(mode)
            mWrappedSystemCallback.onDestroyActionMode(mode)
        }

        override fun onGetContentRect(mode: ActionMode, view: View, outRect: Rect) {
            when {
                mWrappedCustomCallback is ActionMode.Callback2 -> mWrappedCustomCallback.onGetContentRect(
                    mode,
                    view,
                    outRect
                )
                mWrappedSystemCallback is ActionMode.Callback2 -> mWrappedSystemCallback.onGetContentRect(
                    mode,
                    view,
                    outRect
                )
                else -> super.onGetContentRect(mode, view, outRect)
            }
        }
    }

    fun setContent(htmlContent: String) {
        try {
            loadDataWithBaseURL(CONTENT_SCHEME, htmlContent, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setJavascriptInterface(obj: WebJsInterface) {
        val bridge = WebInitSdk.instance.getBridgeObj()
        addJavascriptInterface(obj, bridge)
    }

    fun exec(trigger: String) {
        if (isReady) {
            load(trigger)
        } else {
            WaitLoad(trigger).executeOnExecutor(THREAD_POOL)
        }
    }

    private fun load(trigger: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null)
        } else {
            loadUrl(trigger)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class WaitLoad(private val mTrigger: String) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            while (!this@BaseWebView.isReady) {
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
                val obj = Object()
                obj.wait(ms)
            } catch (ignore: InterruptedException) {
            }

        }
    }

    override fun loadUrl(url: String?) {
        super.loadUrl(url)
        Log.e(TAG, "BaseWebView load url: " + url!!)
        resetAllStateInternal(url)
    }

    override fun loadUrl(url: String?, additionalHttpHeaders: Map<String, String>) {
        super.loadUrl(url, additionalHttpHeaders)
        Log.e(TAG, "BaseWebView load url: " + url!!)
        resetAllStateInternal(url)
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

    fun dispatchEvent(params: Map<*, *>) {
        loadJS("dj.dispatchEvent", params)
    }

    private fun resetAllStateInternal(url: String?) {
        if (!TextUtils.isEmpty(url) && url!!.startsWith("javascript:")) {
            return
        }
        resetAllState()
    }

    // 加载url时重置touch状态
    protected fun resetAllState() {
        isTouchByUser = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> isTouchByUser = true
        }
        return super.onTouchEvent(event)
    }

    inner class DWebViewClient : WebViewClient() {

        /**
         * url重定向会执行此方法以及点击页面某些链接也会执行此方法
         *
         * @return true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载 false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
         */
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.e(TAG, "shouldOverrideUrlLoading url: " + url!!)
            // 当前链接的重定向, 通过是否发生过点击行为来判断
            if (!isTouchByUser) {
                return super.shouldOverrideUrlLoading(view, url)
            }
            // 如果链接跟当前链接一样，表示刷新
            if (getUrl() == url) {
                return super.shouldOverrideUrlLoading(view, url)
            }
            if (handleLinked(url)) {
                return true
            }
            if (mWebViewCallBack != null && mWebViewCallBack!!.overrideUrlLoading(view!!, url)) {
                return true
            }
            // 控制页面中点开新的链接在当前webView中打开
            view!!.loadUrl(url, mHeaders)
            return true
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            Log.e(TAG, "shouldOverrideUrlLoading url: " + request.url)
            // 当前链接的重定向
            if (!isTouchByUser) {
                return super.shouldOverrideUrlLoading(view, request)
            }
            // 如果链接跟当前链接一样，表示刷新
            if (url == request.url.toString()) {
                return super.shouldOverrideUrlLoading(view, request)
            }
            if (handleLinked(request.url.toString())) {
                return true
            }
            if (mWebViewCallBack != null && mWebViewCallBack!!.overrideUrlLoading(view, request.url.toString())) {
                return true
            }
            // 控制页面中点开新的链接在当前webView中打开
            view.loadUrl(request.url.toString(), mHeaders)
            return true
        }

        /**
         * 支持电话、短信、邮件、地图跳转，跳转的都是手机系统自带的应用
         */
        private fun handleLinked(url: String): Boolean {
            if (url.startsWith(SCHEME_TEL)
                || url.startsWith(SCHEME_SMS)
                || url.startsWith(SCHEME_MAILTO)
                || url.startsWith(SCHEME_GEO)
            ) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    context?.startActivity(intent)
                } catch (ignored: ActivityNotFoundException) {
                    ignored.printStackTrace()
                }

                return true
            }
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            Log.e(TAG, "onPageFinished url:" + url!!)
            if (!TextUtils.isEmpty(url) && url.startsWith(CONTENT_SCHEME)) {
                isReady = true
            }
            if (mWebViewCallBack != null) {
                mWebViewCallBack!!.pageFinished(url)
            }
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            Log.e(TAG, "onPageStarted url: $url")
            if (mWebViewCallBack != null) {
                mWebViewCallBack!!.pageStarted(url)
            }
        }

        override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
            super.onScaleChanged(view, oldScale, newScale)
        }

        @TargetApi(21)
        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            return shouldInterceptRequest(view, request.url.toString())
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            return null
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            Log.e(TAG, "webview error$errorCode + $description")
            if (mWebViewCallBack != null) {
                mWebViewCallBack!!.onError()
            }
        }

        override fun onReceivedSslError(
            webView: WebView?,
            handler: SslErrorHandler,
            error: com.tencent.smtt.export.external.interfaces.SslError?
        ) {
            val channel = ""

            if (!TextUtils.isEmpty(channel) && channel == "play.google.com") {
                val builder = AlertDialog.Builder(context)
                var message = context.getString(R.string.ssl_error)
                when (error!!.primaryError) {
                    SslError.SSL_UNTRUSTED -> message = context.getString(R.string.ssl_error_not_trust)
                    SslError.SSL_EXPIRED -> message = context.getString(R.string.ssl_error_expired)
                    SslError.SSL_IDMISMATCH -> message = context.getString(R.string.ssl_error_mismatch)
                    SslError.SSL_NOTYETVALID -> message = context.getString(R.string.ssl_error_not_valid)
                }
                message += context.getString(R.string.ssl_error_continue_open)

                builder.setTitle(R.string.ssl_error)
                builder.setMessage(message)
                builder.setPositiveButton(R.string.continue_open) { _, _ -> handler.proceed() }
                builder.setNegativeButton(R.string.cancel) { _, _ -> handler.cancel() }
                val dialog = builder.create()
                dialog.show()
            } else {
                handler.proceed()
            }
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    companion object {

        private val THREAD_POOL = Executors.newSingleThreadExecutor()

        private const val TAG = "BaseWebView"

        const val CONTENT_SCHEME = "file:///android_asset/"

        private const val SCHEME_SMS = "sms:"
    }
}