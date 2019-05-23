package com.plumcookingwine.repo

import android.app.Application
import com.plumcookingwine.network.helper.NetworkHelper
import com.plumcookingwine.repo.service.interceptor.PublicParamsInterceptor
import okhttp3.Interceptor

class RepoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkHelper.init(
            applicationContext,
            "https://t3.fsyuncai.com/api/mobile/",
            true,
            mutableListOf<Interceptor>(PublicParamsInterceptor()),
            null
        )
    }
}