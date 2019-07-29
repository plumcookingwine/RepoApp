package com.plumcookingwine.webview.bridge

import android.content.Context
import android.os.Handler
import android.webkit.JavascriptInterface
import java.lang.Exception

class JsInterface(private val mContext: Context) {


    private lateinit var mHandler: Handler

    private var mCommand: JavascriptCommand? = null


    /**
     * 命令执行方法
     */
    @JavascriptInterface
    fun post(cmd: String, params: String) {
        mHandler.post {
            try {
                if (mCommand != null) {
                    mCommand!!.exec(mContext, cmd, params)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setJavascriptCommand(command: JavascriptCommand) {
        this.mCommand = command
    }

    interface JavascriptCommand {

        fun exec(context: Context, cmd: String, params: String)
    }
}