package com.plumcookingwine.repo.view.user

import android.widget.Toast
import com.plumcookingwine.base.view.BasePresenter
import com.plumcookingwine.network.utils.ACache
import com.plumcookingwine.repo.RepoApp

class LoginPresenter(view: LoginView) : BasePresenter<LoginView>(view) {


    fun login(username: String?, password: String?) {
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(RepoApp.context, "用户名或密码错误", Toast.LENGTH_SHORT).show()
            return
        }
        ACache.get(RepoApp.context).put("username", username)
        mView.login(username)
    }
}