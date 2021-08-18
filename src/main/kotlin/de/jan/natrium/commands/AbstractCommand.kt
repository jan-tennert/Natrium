package de.jan.natrium.commands

import java.time.Duration

interface AbstractCommand {

    /**
     * If set, users can only use this command every [timeout] Duration
     */
    val timeout: Duration
        get() = Duration.ZERO

}