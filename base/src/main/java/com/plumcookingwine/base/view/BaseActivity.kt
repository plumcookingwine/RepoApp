package com.plumcookingwine.base.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

abstract class BaseActivity<P : BasePresenter<*>> : RxAppCompatActivity(),
    BaseView {

    lateinit var mPresenter: P

    abstract fun resLayoutId(): Int

    abstract fun init()

    abstract fun logic()

    protected abstract fun initPresenter(): P


    private lateinit var dialog: AlertDialog

    override fun getNetLifecycle(): LifecycleProvider<*> {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resLayoutId())
        mPresenter = initPresenter()
        dialog = AlertDialog.Builder(this).setCancelable(false)
            .setMessage("加载中")
            .create()
        init()
        logic()
    }

    override fun showLoading(loadingText: String) {
        Log.i("TAG", "show")
        dialog.setMessage(loadingText)
        if (dialog.isShowing.not()) {
            dialog.show()
        }

    }

    override fun hideLoading() {
        Log.i("TAG", "hide")
        if (dialog.isShowing) {
            dialog.dismiss()
        }

    }

    override fun onError(text: String) {
        Log.i("TAG", "error hide")
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
        @Suppress("UNCHECKED_CAST")
        this.mPresenter = presenter as P
    }


}