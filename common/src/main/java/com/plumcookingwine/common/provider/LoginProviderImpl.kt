package com.plumcookingwine.common.provider

import com.alibaba.android.arouter.launcher.ARouter
import com.plumcookingwine.common.interfaces.LoginListener
import com.plumcookingwine.common.provider.LoginProvider.Companion.mLoginListener

class LoginProviderImpl : LoginProvider {

    override fun getRegisterLoginListener(): List<LoginListener> {
        return mLoginListener
    }

    override fun isLogin(): Boolean {
        return false
    }

    override fun registerLoginListener(loginListener: LoginListener) {
        mLoginListener.add(loginListener)
    }

    override fun login() {
        ARouter.getInstance().build("/user/login").navigation()
    }

}