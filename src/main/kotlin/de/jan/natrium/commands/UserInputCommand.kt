package de.jan.natrium.commands

import java.time.Duration

interface UserInputCommand : Command {

    /**
     * This is the command syntax which can be used for things like a help command.
     */
    var syntax: String

    /**
     * If set, users can only use this command every [timeout] Duration
     */
    val timeout: Duration
        get() = Duration.ZERO

    /**
     * This is the command description
     */
    var description: String

}