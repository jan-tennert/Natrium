package de.jan.natrium.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.Interaction
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction
import net.dv8tion.jda.api.utils.AttachmentOption
import java.io.File
import java.io.InputStream

interface HybridCommand : ISlashCommand {

    fun run(result: CommandResult)

}

data class CommandResult(val channel: MessageChannel, val args: List<String>, val user: User, val member: Member? = null, val origin: CommandOrigin)