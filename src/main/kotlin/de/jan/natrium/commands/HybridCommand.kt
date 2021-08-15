package de.jan.natrium.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.AttachmentOption
import java.io.File
import java.io.InputStream

interface HybridCommand : ISlashCommand {

    val autoAcknowledge: Boolean
        get() = true

    fun run(result: CommandResult)

}

data class CommandResult(val channel: MessageChannel, val args: List<String>, val user: User, val member: Member? = null, val origin: CommandOrigin)

/**
 * This class will send messages over an [InteractionHook], if it's coming from a slash command
 * or over a [MessageChannel] if it's a normal command
 */
class CommandOrigin(val channel: MessageChannel? = null, val hook: InteractionHook? = null) {

    /**
     * Will send all messages as ephemeral, if possible
     */
    var ephemeral: Boolean = false
        set(value) {
            field = value
            hook?.setEphemeral(true)
        }

    fun sendMessage(content: String): RestAction<Message> {
        if(channel != null) return channel.sendMessage(content)
        if(hook != null) return hook.sendMessage(content)
        throw IllegalStateException("There has to be a channel or an interaction hook to send messages")
    }

    fun sendMessage(message: Message): RestAction<Message> {
        if(channel != null) return channel.sendMessage(message)
        if(hook != null) return hook.sendMessage(message)
        throw IllegalStateException("There has to be a channel or an interaction hook to send messages")
    }

    fun sendFile(file: File, vararg attachmentOptions: AttachmentOption): RestAction<Message> {
        if(channel != null) return channel.sendFile(file, *attachmentOptions)
        if(hook != null) return hook.sendFile(file, *attachmentOptions)
        throw IllegalStateException("There has to be a channel or an interaction hook to send messages")
    }

    fun sendFile(file: File, fileName: String, vararg attachmentOptions: AttachmentOption): RestAction<Message> {
        if(channel != null) return channel.sendFile(file, fileName, *attachmentOptions)
        if(hook != null) return hook.sendFile(file, fileName, *attachmentOptions)
        throw IllegalStateException("There has to be a channel or an interaction hook to send messages")
    }

    fun sendFile(byteArray: ByteArray, name: String, vararg attachmentOptions: AttachmentOption): RestAction<Message> {
        if(channel != null) return channel.sendFile(byteArray, name, *attachmentOptions)
        if(hook != null) return hook.sendFile(byteArray, name, *attachmentOptions)
        throw IllegalStateException("There has to be a channel or an interaction hook to send messages")
    }

    fun sendFile(input: InputStream, name: String, vararg attachmentOptions: AttachmentOption): RestAction<Message> {
        if(channel != null) return channel.sendFile(input, name, *attachmentOptions)
        if(hook != null) return hook.sendFile(input, name, *attachmentOptions)
        throw IllegalStateException("There has to be a channel or an interaction hook to send messages")
    }

    fun sendMessageEmbeds(vararg embeds: MessageEmbed) = sendMessageEmbeds(embeds.toList())

    fun sendMessageEmbeds(embeds: Collection<MessageEmbed>): RestAction<Message>  {
        if(channel != null) return channel.sendMessageEmbeds(embeds.toList())
        if(hook != null) return hook.sendMessageEmbeds(embeds.toList())
        throw IllegalStateException("There has to be a channel or an interaction hook to send messages")
    }

    fun sendMessageEmbed(embed: EmbedBuilder.() -> Unit) = sendMessageEmbeds(EmbedBuilder().apply(embed).build())

}