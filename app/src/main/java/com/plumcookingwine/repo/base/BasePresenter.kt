package com.plumcookingwine.repo.base

import com.plumcookingwine.network.callback.ICommonInterface
import com.trello.rxlifecycle2.LifecycleProvider

abstract class BasePresenter<V : BaseView>(view: V) : ICommonInterface {

    val mView: V = view

    init {
        @Suppress("LeakingThis")
        view.setPresenter(this@BasePresenter)
    }

    override fun onSubscribe() {
        mView.showLoading()
    }

    override fun onComplete() {
        mView.hideLoading()
    }

    override fun lifecycle(): LifecycleProvider<*> {
        return mView.getNetLifecycle()
    }
}