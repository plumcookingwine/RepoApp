package com.plumcookingwine.repo.view.file

import com.plumcookingwine.repo.R
import com.plumcookingwine.repo.base.BaseActivity
import com.duke.dfileselector.activity.DefaultSelectorActivity
import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.duke.dfileselector.util.FileSelectorUtils
import kotlinx.android.synthetic.main.activity_selector_file.*


class SelectorFileActivity : BaseActivity<SelectorFilePresenter>(), SelectorFileView {

    companion object {

        private val TAG = SelectorFileActivity::class.java.simpleName
    }

    private var isRegister = false

    private val intentFilter = IntentFilter(DefaultSelectorActivity.FILE_SELECT_ACTION)


    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent == null) {
                return
            }
            if (DefaultSelectorActivity.FILE_SELECT_ACTION == intent.action) {
                printData(DefaultSelectorActivity.getDataFromIntent(intent))
                finish()
            }
        }
    }

    override fun resLayoutId(): Int {
        return R.layout.activity_selector_file
    }

    override fun init() {

    }

    override fun logic() {
        btnFile.setOnClickListener {
            DefaultSelectorActivity.startActivityForResult(this, true, false, 1)   //包含广播
        }
    }

    override fun onResume() {
        super.onResume()
        if (isRegister.not()) {
            registerReceiver(receiver, intentFilter)
            isRegister = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegister) {
            unregisterReceiver(receiver)
            isRegister = false
        }
    }

    override fun initPresenter(): SelectorFilePresenter {
        return SelectorFilePresenter(this)
    }

    private fun printData(list: ArrayList<String>) {
        if (FileSelectorUtils.isEmpty(list)) {
            return
        }
        val size = list.size
        Log.v(TAG, "获取到数据-开始 size = $size")
        val stringBuffer = StringBuffer("选中的文件：\r\n")
        for (i in 0 until size) {
            Log.v(TAG, (i + 1).toString() + " = " + list[i])
            stringBuffer.append(list[i])
            stringBuffer.append("\r\n")
        }
        Toast.makeText(this, stringBuffer.toString(), Toast.LENGTH_SHORT).show()
        Log.v(TAG, "获取到数据-结束")
    }
}
