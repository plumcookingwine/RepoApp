package com.plumcookingwine.webview.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.plumcookingwine.webview.R

class WebActivity : AppCompatActivity() {

    private var fragment: WebFragment? = null

    private var mTitle: String? = null

    private var mUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        mTitle = intent?.getStringExtra("title")
        mUrl = intent?.getStringExtra("url")

        if (fragment == null) {
            fragment = WebFragment.newInstance(mUrl, mTitle)
            supportFragmentManager.beginTransaction()
                .add(R.id.mRlContainer, fragment!!, WebFragment::class.java.simpleName)
                .commitNow()
        }
    }

    override fun onBackPressed() {
        if (fragment?.canGoBack() == false) {
            super.onBackPressed()
        }
    }
}
