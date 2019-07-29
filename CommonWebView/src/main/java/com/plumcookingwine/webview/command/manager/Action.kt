package com.plumcookingwine.webview.command.manager

/**
 * Created by xud on 2018/10/31
 */
interface Action<T> {
    fun call(t: T)
}

