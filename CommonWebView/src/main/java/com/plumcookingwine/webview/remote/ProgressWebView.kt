package com.plumcookingwine.webview.remote

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.plumcookingwine.webview.client.ProgressWebChromeClient
import com.plumcookingwine.webview.progress.IndicatorHandler
import com.plumcookingwine.webview.progress.WebProgressBar

/**
 * Created by xud on 2018/1/22.
 */

class ProgressWebView : BaseWebView {

    private var indicatorHandler: IndicatorHandler? = null
    private var progressBar: WebProgressBar? = null

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            val progress = msg.obj as Int
            indicatorHandler!!.progress(progress)
        }
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun getHandler(): Handler {
        return handler
    }

    private fun init() {
        progressBar = WebProgressBar(context)
        progressBar!!.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        progressBar!!.visibility = View.GONE
        addView(progressBar)
        indicatorHandler = IndicatorHandler.getInstance().inJectProgressView(progressBar)
        webChromeClient = ProgressWebChromeClient(handler)
    }
}
