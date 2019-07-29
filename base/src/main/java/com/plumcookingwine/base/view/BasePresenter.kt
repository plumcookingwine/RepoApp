package com.plumcookingwine.base.view

import com.plumcookingwine.network.callback.ICommonInterface
import com.plumcookingwine.network.exception.ApiErrorModel
import com.trello.rxlifecycle2.LifecycleProvider

abstract class BasePresenter<V : BaseView>(view: V) : ICommonInterface {

    val mView: V = view

    init {
        @Suppress("LeakingThis")
        view.setPresenter(this@BasePresenter)
    }

    override fun onSubscribe(loadingMsg: String) {
        mView.showLoading(loadingMsg)
    }

    override fun onComplete() {
        mView.hideLoading()
    }

    override fun lifecycle(): LifecycleProvider<*> {
        return mView.getNetLifecycle()
    }

    override fun isDefaultError(): Boolean {
        return false
    }

    override fun error(msg: ApiErrorModel) {
        return mView.onError(msg.message)
    }
}