package com.plumcookingwine.webview.command

import android.content.Context
import android.text.TextUtils
import com.plumcookingwine.webview.callback.AidlError
import com.plumcookingwine.webview.callback.ResultBack
import com.plumcookingwine.webview.command.manager.Command
import com.plumcookingwine.webview.command.manager.Commands
import com.plumcookingwine.webview.common.WebConstants

import java.util.HashMap

/**
 * Created by xud on 2017/12/16.
 */

class AccountLevelCommands : Commands() {

    // 获取native data
    private val appDataProviderCommand = object : Command {
        override fun name(): String {
            return "appDataProvider"
        }

        override fun exec(context: Context, params: Map<String, String>, resultBack: ResultBack) {
            try {
                var callbackName = ""
                if (params["type"] == null) {
                    val aidlError = AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM)
                    resultBack.onResult(WebConstants.FAILED, this.name(), aidlError)
                    return
                }
                if (params[WebConstants.WEB2NATIVE_CALLBACk] != null) {
                    callbackName = params[WebConstants.WEB2NATIVE_CALLBACk]!!.toString()
                }
                val type = params["type"]!!.toString()
                val map = HashMap<String, String>()
                when (type) {
                    "account" -> {
                        map.put("accountId", "test123456")
                        map.put("accountName", "xud")
                    }
                }
                if (!TextUtils.isEmpty(callbackName)) {
                    map.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName)
                }
                resultBack.onResult(WebConstants.SUCCESS, this.name(), map)
            } catch (e: Exception) {
                e.printStackTrace()
                val aidlError = AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM)
                resultBack.onResult(WebConstants.FAILED, this.name(), aidlError)
            }

        }
    }

    init {
        registerCommands()
    }

    internal fun registerCommands() {
        registerCommand(appDataProviderCommand)
    }

    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_ACCOUNT
    }
}
