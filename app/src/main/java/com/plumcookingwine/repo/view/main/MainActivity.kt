package com.plumcookingwine.repo.view.main

import android.widget.Toast
import com.plumcookingwine.repo.R
import com.plumcookingwine.repo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainPresenter>(), MainView {

    // 模拟分页
    private var page = 0

    override fun success(json: String) {
        page++
        Toast.makeText(this, "success === $json", Toast.LENGTH_SHORT).show()
    }

    override fun initPresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun resLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init() {

        btnRequest.setOnClickListener {
            mPresenter.request(page)
        }

        btnRetry.setOnClickListener {
            mPresenter.testRetry()
        }
    }

    override fun logic() {
    }
}
