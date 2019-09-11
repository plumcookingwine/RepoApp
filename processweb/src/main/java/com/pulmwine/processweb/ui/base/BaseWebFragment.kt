package com.pulmwine.processweb.ui.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pulmwine.processweb.R
import com.pulmwine.processweb.callback.WebViewCallBack
import com.pulmwine.processweb.command.CommandDispatcher
import com.pulmwine.processweb.command.MainLooper
import com.pulmwine.processweb.common.WebConstants
import com.pulmwine.processweb.lifecycle.DefaultWebLifeCycleImpl
import com.pulmwine.processweb.lifecycle.WebLifeCycle
import com.pulmwine.processweb.weiget.BaseWebView
import com.tencent.smtt.sdk.WebView

/**
 * Created by xud on 2017/12/16.
 */

abstract class BaseWebFragment : BaseFragment(), WebViewCallBack {

    private var webLifeCycle: WebLifeCycle? = null

    private var webView: BaseWebView? = null

    private var webUrl: String? = null

    @LayoutRes
    protected abstract fun layoutRes(): Int

    private var dispatcherCallBack: CommandDispatcher.DispatcherCallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            webUrl = bundle.getString(WebConstants.INTENT_TAG_URL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutRes(), container, false)
        webView = view.findViewById(R.id.web_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webLifeCycle = DefaultWebLifeCycleImpl(webView)
        webView!!.registerWebViewCallBack(this)
        CommandDispatcher.getInstance().initAidlConnect(
            context
        ) {
            MainLooper.runOnUiThread(Runnable {
                loadUrl()
            })
        }
    }

    override fun commandLevel(): Int {
        return WebConstants.LEVEL_BASE
    }

    private fun loadUrl() {
        webView!!.loadUrl(webUrl)
    }

    override fun onResume() {
        super.onResume()
        webView!!.dispatchEvent("pageResume")
        webLifeCycle?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView!!.dispatchEvent("pagePause")
        webLifeCycle?.onPause()
    }

    override fun onStop() {
        super.onStop()
        webView!!.dispatchEvent("pageStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView!!.dispatchEvent("pageDestroy")
        webLifeCycle?.onDestroy()
    }

    override fun pageStarted(url: String) {

    }

    override fun pageFinished(url: String) {

    }

    override fun overrideUrlLoading(view: WebView, url: String): Boolean {
        return false
    }

    override fun onError() {

    }

    override fun exec(context: Context, commandLevel: Int, cmd: String, params: String, webView: WebView) {
        CommandDispatcher.getInstance().exec(context, commandLevel, cmd, params, webView, dispatcherCallBack)
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            onBackHandle()
        } else false
    }

    private fun onBackHandle(): Boolean {
        if (webView != null) {
            return if (webView!!.canGoBack()) {
                webView!!.goBack()
                true
            } else {
                false
            }
        }
        return false
    }

}
