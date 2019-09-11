package com.pulmwine.processweb.lifecycle

import android.os.Looper
import android.view.ViewGroup
import com.tencent.smtt.sdk.WebView

/**
 * Created by xud on 2018/2/7.
 */

class DefaultWebLifeCycleImpl(private val mWebView: WebView?) : WebLifeCycle {

    override fun onResume() {
        if (this.mWebView != null) {
            this.mWebView.onResume()
        }
    }

    override fun onPause() {
        if (this.mWebView != null) {
            this.mWebView.onPause()
        }
    }

    override fun onDestroy() {
        if (this.mWebView != null) {
            clearWebView(this.mWebView)
        }
    }

    private fun clearWebView(m: WebView?) {
        var m: WebView? = m ?: return
        if (Looper.myLooper() != Looper.getMainLooper())
            return
        m!!.stopLoading()
        if (m.handler != null) {
            m.handler.removeCallbacksAndMessages(null)
        }
        m.removeAllViews()
        val mViewGroup: ViewGroup? = m.parent as? ViewGroup
        mViewGroup?.removeView(m)
        m.webChromeClient = null
        m.webViewClient = null
        m.tag = null
        m.clearHistory()
        m.destroy()
        m = null
    }
}
