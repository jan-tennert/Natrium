package de.jan.natrium.commands

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

interface HybridCommand : ISlashCommand, AbstractCommand {

    fun run(result: CommandResult)

}

data class CommandResult(val channel: MessageChannel, val args: List<String>, val user: User, val member: Member? = null, val origin: CommandOrigin)