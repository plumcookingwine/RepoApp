package com.pulmwine.processweb

import android.app.Application
import android.content.Context
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk

class WebInitSdk private constructor() {

    companion object {

        val instance by lazy { WebInitSdk() }

        private var mBridgeObj: String? = null

        private var mApplication: Application? = null

        /**
         * 初始化X5内核
         */
        private fun initX5(context: Application) {
            val map = mutableMapOf<String, Any>()
            map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
            QbSdk.initTbsSettings(map)
            QbSdk.initX5Environment(context, null)
        }

        fun init(context: Application, bridge: String) {
            initX5(context)
            this.mBridgeObj = bridge
            this.mApplication = context
        }
    }

    fun getBridgeObj(): String {
        if (mBridgeObj.isNullOrEmpty()) {
            throw Throwable("please init WebInitSdk in your application")
        }
        return mBridgeObj!!
    }

    fun getContext(): Context {
        if (mApplication == null) {
            throw Throwable("please init WebInitSdk in your application")
        }
        return mApplication!!.applicationContext
    }

}