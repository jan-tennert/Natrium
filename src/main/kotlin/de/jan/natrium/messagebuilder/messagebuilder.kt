package de.jan.natrium.messagebuilder

import de.jan.natrium.interactions.ActionRowBuilder
import de.jan.natrium.interactions.RowBuilder
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.ComponentLayout
import net.dv8tion.jda.internal.entities.DataMessage
import java.util.EnumSet

class KMessageBuilder(private val jda: JDA? = null) {

    var tts = false
    var content = ""
    var nonce = ""
    val embeds = mutableListOf<MessageEmbed>()
    val allowedMentions = EnumSet.noneOf(Message.MentionType::class.java)
    val mentionedUsers = mutableListOf<String>()
    val mentionedRoles = mutableListOf<String>()
    val actionRows = mutableListOf<ComponentLayout>()

    fun build() : Message = DataMessage(tts, content, nonce, embeds, allowedMentions, mentionedUsers.toTypedArray(), mentionedRoles.toTypedArray(), actionRows.toTypedArray())

    fun mention(mention: IMentionable) = if(mention is Role) mentionedRoles.add(mention.id).run { Unit } else if(mention is User) mentionedUsers.add(mention.id).run { Unit } else Unit

    fun setActionRows(jda: JDA? = this.jda, builder: ActionRowBuilder.() -> Unit) {
        actionRows.clear()
        actionRows += ActionRowBuilder(jda).apply(builder).rows
    }

    fun addActionRow(jda: JDA? = this.jda, builder: RowBuilder.() -> Unit) {
        actionRows += ActionRow.of(RowBuilder(jda).apply(builder).components)
    }

    fun embed(builder: EmbedBuilder.() -> Unit) {
        embeds += EmbedBuilder().apply(builder).build()
    }

    fun codeblock(code: String, language: String) {
        content += """
            ```$language
            $code
            ```
        """.trimIndent()
    }

    fun newLine() {
        content += "\n"
    }

    fun italic(text: String) {
        content += "*$text*"
    }

    fun bold(text: String) {
        content += "**$text**"
    }

    fun strikethrough(text: String) {
        content += "~~$text~~"
    }

    fun underline(text: String) {
        content += "__${text}__"
    }

    fun block(text: String) {
        content += "`$text`"
    }

}