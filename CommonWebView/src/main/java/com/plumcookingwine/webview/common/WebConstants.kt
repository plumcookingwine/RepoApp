package com.plumcookingwine.webview.common

/**
 * Created by xud on 2017/8/16.
 */

object WebConstants {

    const val LEVEL_UI = 0 // UI Command
    const val LEVEL_BASE = 1 // 基础level
    const val LEVEL_ACCOUNT = 2 // 涉及到账号相关的level

    const val CONTINUE = 2 // 继续分发command
    const val SUCCESS = 1 // 成功
    const val FAILED = 0 // 失败
    const val EMPTY = "" // 无返回结果

    const val WEB2NATIVE_CALLBACk = "callback"
    const val NATIVE2WEB_CALLBACK = "callbackname"

    const val ACTION_EVENT_BUS = "eventBus"

    const val INTENT_TAG_TITLE = "title"
    const val INTENT_TAG_URL = "url"

    object SCHEME {
        const val CONTENT_SCHEME = "file:///android_asset/"
    }

    object ERRORCODE {
        const val NO_METHOD = -1000
        const val NO_AUTH = -1001
        const val NO_LOGIN = -1002
        const val ERROR_PARAM = -1003
        const val ERROR_EXCEPTION = -1004
    }

    object ERRORMESSAGE {
        const val NO_METHOD = "方法找不到"
        const val NO_AUTH = "方法权限不够"
        const val NO_LOGIN = "尚未登录"
        const val ERROR_PARAM = "参数错误"
        const val ERROR_EXCEPTION = "未知异常"
    }
}
