package com.pulmwine.processweb.command.impl

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.Toast
import com.pulmwine.processweb.command.Command
import com.pulmwine.processweb.command.Commands
import com.pulmwine.processweb.bridge.call.ResultCallback
import com.pulmwine.processweb.common.WebConstants
import com.pulmwine.processweb.utils.WebUtils

class UICommands : Commands() {

    /**
     * 实现命令，用户toast提示
     */
    private val mToastCommand by lazy {
        object : Command {
            override fun exec(context: Context, map: Map<String, Any>, resultCallback: ResultCallback) {
                Toast.makeText(context, map["message"].toString(), Toast.LENGTH_SHORT).show()
            }

            override fun name(): String {
                return "showToast"
            }
        }
    }

    private val mDialogCommand by lazy {
        object : Command {
            override fun exec(context: Context, params: Map<String, Any>, resultCallback: ResultCallback) {

                if (WebUtils.isNotNull(params)) {
                    val title = params.get("title") as String
                    val content = params.get("content") as String
                    var canceledOutside = 1
                    if (params.get("canceledOutside") != null) {
                        canceledOutside = (params.get("canceledOutside") as Double).toInt()
                    }
                    val buttons = params.get("buttons") as List<MutableMap<String, String>>
                    val callbackName = params.get(WebConstants.WEB2NATIVE_CALLBACk) as String
                    if (!TextUtils.isEmpty(content)) {
                        val dialog = AlertDialog.Builder(context)
                            .setTitle(title)
                            .setMessage(content)
                            .create()
                        dialog.setCanceledOnTouchOutside(canceledOutside == 1)
                        if (WebUtils.isNotNull(buttons)) {
                            for (i in buttons.indices) {
                                val button = buttons[i]
                                val buttonWhich = getDialogButtonWhich(i)

                                if (buttonWhich == 0) return

                                dialog.setButton(buttonWhich, button["title"]) { dialog, which ->
                                    button[WebConstants.NATIVE2WEB_CALLBACK] = (callbackName)
                                    resultCallback.onResult(WebConstants.SUCCESS, name(), button)
                                }
                            }
                        }
                        dialog.show()
                    }
                }
            }

            override fun name(): String {
                return "showDialog"
            }
        }
    }

    init {
        registerCommand(mToastCommand)
        registerCommand(mDialogCommand)
    }

    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_UI
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