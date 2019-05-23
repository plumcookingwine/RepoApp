package com.plumcookingwine.network.callback

import android.widget.Toast
import com.plumcookingwine.network.cookie.CookieResultListener
import com.plumcookingwine.network.exception.ApiErrorModel
import com.plumcookingwine.network.helper.NetworkHelper

abstract class INetworkCallback<T>(private val commonInterface: ICommonInterface) {

    /**
     * 通用的view层的接口，用于与view交互
     */
    fun getCommonInter(): ICommonInterface {
        return commonInterface
    }

    /**
     * 请求成功回调
     */
    abstract fun onSuccess(obj: T, cookieListener: CookieResultListener)

    /**
     * 缓存回调
     */
    open fun onCache(json: String) {

    }


    /**
     * 可重写此方法，自定义错误事件
     */
    open fun onError(err: ApiErrorModel?) {
        getCommonInter().onComplete()
        Toast
            .makeText(
                NetworkHelper.instance.getContext(),
                err?.message ?: "未知错误",
                Toast.LENGTH_SHORT
            )
            .show()
    }


}