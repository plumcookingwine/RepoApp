package com.pulmwine.processweb.atest.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pulmwine.processweb.R
import com.pulmwine.processweb.atest.*
import kotlinx.android.synthetic.main.activity_common_test.*

class CommandTestActivity : AppCompatActivity(), View.OnClickListener {

    private val mInvoker by lazy { Invoker() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_test)
        mBtnPlay.setOnClickListener(this)
        mBtnGo.setOnClickListener(this)
        mBtnPause.setOnClickListener(this)
        mBtnStop.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.mBtnPlay -> {
                mInvoker.setCommand(PlayCommand())
            }
            R.id.mBtnPause -> {
                mInvoker.setCommand(PauseCommand())
            }
            R.id.mBtnGo -> {
                mInvoker.setCommand(GoCommand())
            }
            R.id.mBtnStop -> {
                mInvoker.setCommand(StopCommand())
            }
        }
        mInvoker.action()
    }
}
