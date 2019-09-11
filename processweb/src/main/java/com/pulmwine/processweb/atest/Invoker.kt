package com.pulmwine.processweb.atest

class Invoker {

    private var mCommand: Command? = null


    fun setCommand(command: Command) {
        this.mCommand = command
    }


    fun action() {
        mCommand?.execute()
    }
}