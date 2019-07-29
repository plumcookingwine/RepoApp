package com.plumcookingwine.webview.command.manager

import java.util.HashMap

abstract class Commands {

    private val commands: HashMap<String, Command> = HashMap()

    abstract fun getCommandLevel(): Int

    protected fun getCommands(): HashMap<String, Command> {
        return commands
    }

    protected fun registerCommand(command: Command) {
        commands[command.name()] = command
    }
}


