package com.pulmwine.processweb.atest

import android.util.Log

class PauseCommand : Command {

    override fun execute() {
        Log.i("TAG", "PAUSE")
    }

}