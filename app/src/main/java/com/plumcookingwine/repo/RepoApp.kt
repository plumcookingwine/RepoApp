package com.plumcookingwine.repo

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
import com.plumcookingwine.base.BuildConfig
import com.plumcookingwine.base.network.interceptor.PublicParamsInterceptor
import com.plumcookingwine.common.provider.LoginProvider
import com.plumcookingwine.common.provider.LoginProviderImpl
import com.plumcookingwine.repo.view.user.avatar.MediaLoad
import com.pulmwine.processweb.WebInitSdk
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig

@Suppress("unused")
class RepoApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        val processName = getProcessName(Process.myPid())
        if (processName == null) {
            onTerminate()
            throw Throwable("application is be terminate")
        }
        context = applicationContext
        ServiceManager.init(this)
        ServiceManager.publishService(LoginProvider.LOGIN_SERVICE, LoginProviderImpl::class.java.name)

        if (TextUtils.equals(packageName, processName)) {
            initCommon()
        }
        initWeb()
        if (processName.endsWith("remoteweb")) {

        }
    }

    private fun initWeb() {
        WebInitSdk.init(this, "YunYou")
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

        Album.initialize(AlbumConfig.newBuilder(applicationContext)
            .setAlbumLoader(MediaLoad())
            .build())
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