package com.plumcookingwine.webview.command

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.Toast
import com.plumcookingwine.webview.callback.ResultBack
import com.plumcookingwine.webview.command.manager.Command
import com.plumcookingwine.webview.command.manager.Commands
import com.plumcookingwine.webview.common.WebConstants
import com.plumcookingwine.webview.utils.WebUtils

/**
 * Created by xud on 2018/10/31
 */
class UIDependencyCommands : Commands() {

    /**
     * 显示Toast信息
     */
    private val showToastCommand = object : Command {
        override fun name(): String {
            return "showToast"
        }

        override fun exec(context: Context, params: Map<String, String>, resultBack: ResultBack) {
            Toast.makeText(context, params["message"].toString(), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 对话框显示
     */
    private val showDialogCommand = object : Command {
        override fun name(): String {
            return "showDialog"
        }

        override fun exec(context: Context, params: Map<String, String>, resultBack: ResultBack) {
            if (WebUtils.isNotNull(params)) {
                val title = params["title"] as String?
                val content = params["content"] as String?
                var canceledOutside = 1
                if (params["canceledOutside"] != null) {
                    canceledOutside = (params["canceledOutside"] as Double).toInt()
                }
                val buttons = params["buttons"] as List<HashMap<String, String>>?
                val callbackName = params[WebConstants.WEB2NATIVE_CALLBACk] as String?
                if (!TextUtils.isEmpty(content)) {
                    val dialog = AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(content)
                        .create()
                    dialog.setCanceledOnTouchOutside(canceledOutside == 1)
                    if (WebUtils.isNotNull(buttons)) {
                        for (i in buttons!!.indices) {
                            val button: HashMap<String, String> = buttons[i]
                            val buttonWhich = getDialogButtonWhich(i)

                            if (buttonWhich == 0) return

                            dialog.setButton(buttonWhich, button["title"]) { dialog, which ->
                                button.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName!!)
                                resultBack.onResult(WebConstants.SUCCESS, name(), button)
                            }
                        }
                    }
                    dialog.show()
                }
            }
        }

        private fun getDialogButtonWhich(index: Int): Int {
            when (index) {
                0 -> return DialogInterface.BUTTON_POSITIVE
                1 -> return DialogInterface.BUTTON_NEGATIVE
                2 -> return DialogInterface.BUTTON_NEUTRAL
            }
            return 0
        }
    }

    init {
        registCommands()
    }

    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_UI
    }

    fun registCommands() {
        registerCommand(showToastCommand)
        registerCommand(showDialogCommand)
    }
}
