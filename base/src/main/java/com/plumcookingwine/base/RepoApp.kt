package com.plumcookingwine.base

import android.annotation.SuppressLint
import android.app.Application
import com.plumcookingwine.network.helper.NetworkHelper
import okhttp3.Interceptor
import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.limpoxe.support.servicemanager.ServiceManager
import com.plumcookingwine.base.network.interceptor.PublicParamsInterceptor
import com.plumcookingwine.base.provider.LoginProviderImpl
//import com.plumcookingwine.webview.WebSdkInit

class RepoApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        ServiceManager.init(this)
        ServiceManager.publishService("loginService", LoginProviderImpl::class.java.name)

        val processName = getProcessName(Process.myPid())
        Log.i("RePoApp", "ProcessName === $processName")
        if (processName == null) {
            onTerminate()
            throw Throwable("application is be terminate")
        }
        if (TextUtils.equals(packageName, processName)) {
            initCommon()
        }
        if (processName.endsWith("cweb")) {
            initWeb()
        }
    }

    private fun initWeb() {
//        WebSdkInit.init(applicationContext, "dj")
    }

    private fun initCommon() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(applicationContext as Application)

        NetworkHelper.init(
            applicationContext,
            "https://t3.fsyuncai.com/api/mobile/",
            true,
            mutableListOf<Interceptor>(PublicParamsInterceptor()),
            null
        )
    }


    private fun getProcessName(pid: Int): String? {
        val am = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfoList = am.runningAppProcesses ?: return null
        for (processInfo in processInfoList) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return null
    }
}