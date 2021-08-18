package de.jan.natrium.commands

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface StandardCommand : UserInputCommand {

    suspend fun run(msg: Message, args: List<String>)

    override fun build(): CommandData {
        throw UnsupportedOperationException()
    }

}