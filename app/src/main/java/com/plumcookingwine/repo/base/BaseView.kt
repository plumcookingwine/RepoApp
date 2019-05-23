package com.plumcookingwine.repo.base

import com.trello.rxlifecycle2.LifecycleProvider

interface BaseView {

    fun getNetLifecycle(): LifecycleProvider<*>

    fun showLoading()

    fun hideLoading()

    fun onError(text: String)

    fun setPresenter(presenter: BasePresenter<*>)
}