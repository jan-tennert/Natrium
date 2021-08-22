package de.jan.natrium.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.Component
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu
import net.dv8tion.jda.api.requests.restaction.MessageAction
import java.io.File

fun Message.disableAllButtons(filter: (Button) -> Boolean = { true }) = disableAllComponents { it is Button && filter(it) }
fun Message.disableAllSelectionMenus(filter: (SelectionMenu) -> Boolean = { true }) = disableAllComponents { it is SelectionMenu && filter(it) }
fun Message.disableAllComponents(filter: (Component) -> Boolean = { true }): MessageAction {
    val newRows = mutableListOf<ActionRow>()
    for (row in actionRows) {
        val components = mutableListOf<Component>()
        row.components.forEach {
            var component = it
            if(it is SelectionMenu && filter(it)) {
                component = it.asDisabled()
            } else if(it is Button && filter(it)) {
                component = it.asDisabled()
            }
            components += component
        }
        newRows += ActionRow.of(components)
    }
    return editMessageComponents(newRows)
}
fun Message.clearComponents() = editMessageComponents(emptyList())

fun User.sendMessage(message: Message) = openPrivateChannel()
    .flatMap { it.sendMessage(message) }
fun User.sendFile(message: File) = openPrivateChannel()
    .flatMap { it.sendFile(message) }
fun User.sendMessageEmbeds(embeds: Iterable<MessageEmbed>) = openPrivateChannel()
    .flatMap { it.sendMessageEmbeds(embeds.toList()) }
fun User.sendMessageEmbed(builder: EmbedBuilder.() -> Unit) = sendMessageEmbeds(listOf((EmbedBuilder().apply(builder).build())))
fun User.sendMessageEmbeds(vararg embeds: MessageEmbed) = sendMessageEmbeds(embeds.toList())
fun User.sendMessage(message: String) = openPrivateChannel()
    .flatMap { it.sendMessage(message) }