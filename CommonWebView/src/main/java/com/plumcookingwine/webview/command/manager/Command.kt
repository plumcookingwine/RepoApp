package com.plumcookingwine.webview.command.manager

import android.content.Context
import com.plumcookingwine.webview.callback.ResultBack

interface Command {

    companion object {

        const val COMMAND_UPDATE_TITLE = "COMMAND_UPDATE_TITLE"
        const val COMMAND_UPDATE_TITLE_PARAMS_TITLE = "COMMAND_UPDATE_TITLE_PARAMS_TITLE"

    }

    fun name(): String

    fun exec(context: Context, params: Map<String, String>, callback: ResultBack)
}