package com.pulmwine.processweb.utils

import android.app.ActivityManager
import android.content.Context

/**
 * Created by xud on 2018/10/31
 */
object SystemInfoUtil {

    /**
     * 判断当前是否是主进程，context 是 ApplicationContext
     */
    fun inMainProcess(context: Context, pid: Int): Boolean {
        val packageName = context.packageName
        val processName = getProcessName(context, pid)
        return packageName == processName
    }

    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    fun getProcessName(context: Context, pid: Int): String? {

        // ActivityManager
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }
}
