package com.pulmwine.processweb.command

abstract class Commands {

    private val mCommands = mutableMapOf<String, Command>()

    open fun getCommands(): MutableMap<String, Command> {
        return mCommands
    }

    open fun registerCommand(command: Command) {
        mCommands[command.name()] = command
    }

    abstract fun getCommandLevel(): Int

}