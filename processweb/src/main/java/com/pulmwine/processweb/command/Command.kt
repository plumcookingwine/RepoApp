package com.pulmwine.processweb.command

import android.content.Context
import com.pulmwine.processweb.bridge.call.ResultCallback

interface Command {

    fun name(): String

    fun exec(context: Context, map: Map<String, Any>, resultCallback: ResultCallback)
}