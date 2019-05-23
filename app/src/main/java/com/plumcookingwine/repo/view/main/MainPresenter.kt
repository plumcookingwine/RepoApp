package com.plumcookingwine.repo.view.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.plumcookingwine.network.manager.HttpManager
import com.plumcookingwine.network.callback.INetworkCallback
import com.plumcookingwine.network.config.AbsRequestOptions
import com.plumcookingwine.network.cookie.CookieResultListener
import com.plumcookingwine.network.exception.ApiErrorModel
import com.plumcookingwine.network.func.RetryWhenFunc
import com.plumcookingwine.repo.base.BasePresenter
import com.plumcookingwine.repo.entity.MainModel
import com.plumcookingwine.repo.service.MainService
import io.reactivex.Observable
import retrofit2.Retrofit
import java.net.ConnectException

class MainPresenter(view: MainView) : BasePresenter<MainView>(view) {

    /**
     * 测试网络请求
     */
    fun request() {
        HttpManager.instance.doHttpDeal(

            object : AbsRequestOptions<String>() {

                override fun createService(retrofit: Retrofit): Observable<String> {
                    return retrofit.create(MainService::class.java).login(
                        "123", "123", "123", "123"
                    )
                }
            }.apply {
                isCache = true
                cacheUrl = "login"
            },

            object : INetworkCallback<String>(this) {


                override fun onCache(json: String) {
                    mView?.success("cache===$json")
                }

                override fun onSuccess(obj: String, cookieListener: CookieResultListener) {

                    val mainModel = Gson().fromJson<MainModel>(obj, MainModel::class.java)
                    if (mainModel.code == "0") {
                        mView?.success(obj)
                        cookieListener.saveCookie()
                        return
                    }

                    onError(ApiErrorModel((mainModel.code ?: "0").toInt(), mainModel.msg ?: "error"))

                }
//                override fun onError(err: ApiErrorModel?) {
//                    mView?.onError(err?.message ?: "error")
//                }

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