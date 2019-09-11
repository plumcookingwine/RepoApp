package com.plumcookingwine.common.provider

import com.plumcookingwine.common.interfaces.LoginListener

interface LoginProvider {

    companion object {

        const val LOGIN_SERVICE = "LOGIN_SERVICE"

        val mLoginListener = mutableListOf<LoginListener>()
    }


    fun login()

    fun isLogin(): Boolean

    fun registerLoginListener(loginListener: LoginListener)

    fun getRegisterLoginListener(): List<LoginListener>
}