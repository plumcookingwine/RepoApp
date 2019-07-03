package com.plumcookingwine.repo.view.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.plumcookingwine.repo.R
import com.plumcookingwine.repo.base.BaseActivity
import com.plumcookingwine.repo.view.file.SelectorFileActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

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
        backPressure()
        btnFile.setOnClickListener {
            startActivity(Intent(this@MainActivity, SelectorFileActivity::class.java))
        }

        btnRequest.setOnClickListener {
            mPresenter.request(page)
        }

        btnRetry.setOnClickListener {
            mPresenter.testRetry()
        }
    }

    override fun logic() {
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    private fun backPressure() {

        var count = 0
        Observable.create<Int> { emitter ->
            btnBackpressure.setOnClickListener {
                emitter.onNext(count++)
                btnBackpressure.text = "测试背压$count"
            }
        }
            .debounce(800, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val dialog = AlertDialog.Builder(this).setCancelable(false)
                    .setMessage("加载中")
                    .create()
                dialog.show()
                Handler().postDelayed({
                    dialog.dismiss()
                }, 1000)
            }


    }

}
