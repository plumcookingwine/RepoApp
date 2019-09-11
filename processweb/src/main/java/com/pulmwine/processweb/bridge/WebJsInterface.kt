package com.pulmwine.processweb.bridge

import android.content.Context
import android.os.Handler
import android.webkit.JavascriptInterface
import android.widget.Toast
import java.lang.Exception

class WebJsInterface(private val mContext: Context) {


    private val mHandler by lazy { Handler() }


    private var mAidlCommand: AidlCommand? = null


    interface AidlCommand {

        fun exec(context: Context, cmd: String, param: String)
    }


    fun setAidlCommand(aidlCommand: AidlCommand) {
        this.mAidlCommand = aidlCommand
    }

    @JavascriptInterface
    fun post(cmd: String, param: String) {
        mHandler.post {
            try {
                mAidlCommand?.exec(mContext, cmd, param)
            } catch (e: Exception) {
                Toast.makeText(mContext, "执行命令异常$e", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }
    }
}