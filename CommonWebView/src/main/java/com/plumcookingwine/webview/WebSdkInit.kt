package com.plumcookingwine.webview

import android.content.Context
import com.tencent.smtt.sdk.QbSdk

class WebSdkInit {

    companion object {

        private var mInterfaceObjName: String? = null
        val instance by lazy { WebSdkInit() }

        fun init(context: Context, interfaceName: String) {
            this.mInterfaceObjName = interfaceName

            val cb = object : QbSdk.PreInitCallback {
                override fun onCoreInitFinished() {

                }

                override fun onViewInitFinished(p0: Boolean) {

                }
            }
            QbSdk.initX5Environment(context, cb)
        }
    }

    fun getInterfaceName(): String {
        if (mInterfaceObjName == null) {
            throw Throwable("must bu init WebSdkInit in your application")
        }
        return mInterfaceObjName!!
    }


}