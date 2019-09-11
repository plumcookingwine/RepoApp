package com.pulmwine.processweb.command

import android.os.Handler
import android.os.Looper

/**
 * Created by xud on 2018/10/31
 */
open class MainLooper protected constructor(looper: Looper) : Handler(looper) {

    companion object {

        private val instance = MainLooper(Looper.getMainLooper())

        fun runOnUiThread(runnable: Runnable) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                runnable.run()
            } else {
                instance.post(runnable)
            }

        }
    }
}