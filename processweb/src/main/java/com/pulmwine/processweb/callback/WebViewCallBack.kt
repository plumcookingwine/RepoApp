package com.pulmwine.processweb.callback

import android.content.Context
import com.tencent.smtt.sdk.WebView

/**
 * WebView回调统一处理
 * 所有涉及到WebView交互的都必须实现这个callback
 */
interface WebViewCallBack {

    fun commandLevel(): Int

    fun pageStarted(url: String)

    fun pageFinished(url: String)

    fun overrideUrlLoading(view: WebView, url: String): Boolean

    fun onError()

    fun exec(context: Context, commandLevel: Int, cmd: String, params: String, webView: WebView)
}
