package com.plumcookingwine.webview.client

import android.os.Handler
import android.os.Message
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView

/**
 * Created by xud on 2018/1/22.
 */

class ProgressWebChromeClient(private val progressHandler: Handler) : WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        var newP = newProgress
        val message = Message()
        if (newP == 100) {
            message.obj = newP
            progressHandler.sendMessageDelayed(message, 200)
        } else {
            if (newP < 10) {
                newP = 10
            }
            message.obj = newP
            progressHandler.sendMessage(message)
        }
        super.onProgressChanged(view, newP)
    }
}
