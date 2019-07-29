package com.plumcookingwine.base.view

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

    override fun getNetLifecycle(): LifecycleProvider<*> {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resLayoutId())
        mPresenter = initPresenter()
        init()
        logic()
    }

    override fun showLoading(loadingText: String) {
        Log.i("TAG", "show")
        Toast.makeText(this, "show", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        Log.i("TAG", "hide")
        Toast.makeText(this, "hide", Toast.LENGTH_SHORT).show()
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