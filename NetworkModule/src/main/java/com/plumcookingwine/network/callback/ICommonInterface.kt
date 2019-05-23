package com.plumcookingwine.network.callback

import com.trello.rxlifecycle2.LifecycleProvider

interface ICommonInterface {

    fun lifecycle(): LifecycleProvider<*>

    fun onSubscribe()

    fun onComplete()
}