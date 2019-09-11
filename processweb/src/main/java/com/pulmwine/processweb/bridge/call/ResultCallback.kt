package com.pulmwine.processweb.bridge.call

abstract class ResultCallback  {

    abstract fun onResult(status: Int, action: String, result: Any)
}