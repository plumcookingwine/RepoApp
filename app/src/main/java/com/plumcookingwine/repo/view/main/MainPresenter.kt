package com.plumcookingwine.repo.view.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.plumcookingwine.network.manager.HttpManager
import com.plumcookingwine.network.callback.INetworkCallback
import com.plumcookingwine.network.cookie.CookieResultListener
import com.plumcookingwine.network.exception.ApiErrorModel
import com.plumcookingwine.network.func.RetryWhenFunc
import com.plumcookingwine.base.view.BasePresenter
import com.plumcookingwine.repo.entity.MainModel
import com.plumcookingwine.repo.service.options.LoginOpt
import io.reactivex.Observable
import java.net.ConnectException

class MainPresenter(view: MainView) : BasePresenter<MainView>(view) {

    /**
     * 测试网络请求
     */
    fun request(page: Int) {
        HttpManager.instance.doHttpDeal(
            LoginOpt("123", "123", "123", "123"),
            object : INetworkCallback<String>(this) {

                override fun onCache(json: String): Boolean {
                    // 只有第一次请求才缓存(缓存第一页的数据)
                    val isCache = page == 0
                    if (isCache) {
                        mView.success("cache===$json")
                    }
                    return isCache
                }

                override fun onSuccess(obj: String, cookieListener: CookieResultListener) {

                    val mainModel = Gson().fromJson(obj, MainModel::class.java)
                    if (mainModel.code == "2001") {
                        mView.success(obj)
                        cookieListener.saveCookie()
                        return
                    }
                    onError(ApiErrorModel((mainModel.code ?: "0").toInt(), mainModel.msg ?: "error"))
                }
            })
    }

    /**
     * 测试超时重试功能，此处测试完美
     */
    @SuppressLint("CheckResult")
    fun testRetry() {
        Log.i("MainActivity", "version === ${Build.VERSION.SDK_INT}")
        Observable
            .create<String> {
                it.onNext("1")
                it.onNext("2")
                it.onNext("3")
                it.onError(ConnectException("error"))
                it.onNext("4")
            }
            .retryWhen(RetryWhenFunc(3, 100, 100))
            .subscribe(
                {
                    Log.i("MainActivity", "name === $it")
                },
                {
                    Log.i("MainActivity", "error === $it")
                },
                {
                    Log.i("MainActivity", "completed")
                },
                {

                })
    }

}