package com.plumcookingwine.webview.command

import android.content.Context
import com.plumcookingwine.webview.callback.ResultBack
import com.plumcookingwine.webview.command.manager.Command
import com.plumcookingwine.webview.command.manager.Commands
import com.plumcookingwine.webview.common.WebConstants

/**
 * Created by xud on 2017/12/16.
 */

class BaseLevelCommands : Commands() {

    /**
     * 页面路由
     */
    private val pageRouterCommand = object : Command {
        override fun name(): String {
            return "newPage"
        }

        override fun exec(context: Context, params: Map<String, String>, resultBack: ResultBack) {
            val newUrl = params["url"]!!.toString()
            val title = params["title"] as String?
        }
    }

    init {
        registerCommands()
    }

    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_BASE
    }

    internal fun registerCommands() {
        registerCommand(pageRouterCommand)
    }
}
