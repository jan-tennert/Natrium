package de.jan.natrium.commands

import net.dv8tion.jda.api.entities.Message
import java.time.Duration

interface StandardCommand : Command, AbstractCommand {

    suspend fun run(msg: Message, args: List<String>)

}