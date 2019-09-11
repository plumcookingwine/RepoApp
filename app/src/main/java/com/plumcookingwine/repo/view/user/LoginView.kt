package com.plumcookingwine.repo.view.user

import com.plumcookingwine.base.view.BaseView

interface LoginView : BaseView {

    fun login(username: String)
}