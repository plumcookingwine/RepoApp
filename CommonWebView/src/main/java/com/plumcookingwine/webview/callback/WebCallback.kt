package com.plumcookingwine.webview.callback

import android.content.Context
import com.tencent.smtt.sdk.WebView

interface WebCallback {

    fun getCommandLevel(): Int

    fun pageStarted(url: String)

    fun pageFinished(url: String)

    fun overrideUrlLoading(view: WebView, url: String)

    fun onError()

    fun exec(context: Context, commandLevel: Int, cmd: String, params: String, webview: WebView)
}