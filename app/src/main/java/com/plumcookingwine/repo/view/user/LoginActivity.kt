package com.plumcookingwine.repo.view.user

import com.alibaba.android.arouter.facade.annotation.Route
import com.limpoxe.support.servicemanager.ServiceManager
import com.plumcookingwine.base.view.BaseActivity
import com.plumcookingwine.common.provider.LoginProvider
import com.plumcookingwine.repo.R
import kotlinx.android.synthetic.main.activity_login.*

@Route(path = "/user/login")
class LoginActivity : BaseActivity<LoginPresenter>(), LoginView {

    override fun resLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun init() {
    }

    override fun logic() {
        mBtnLogin.setOnClickListener {
            mPresenter.login(
                mEtUser.text.toString().trim(),
                mEtPwd.text.toString().trim()
            )
        }
    }

    override fun initPresenter(): LoginPresenter {
        return LoginPresenter(this)
    }

    override fun login(username: String) {
        val loginProvider = ServiceManager.getService(LoginProvider.LOGIN_SERVICE) as LoginProvider
        loginProvider.getRegisterLoginListener().map {
            it.onLogin()
        }
        finish()
    }
}
