package de.jan.natrium.commands

import net.dv8tion.jda.api.entities.Message

interface StandardCommand : Command, AbstractCommand {

    suspend fun run(msg: Message, args: List<String>)

}