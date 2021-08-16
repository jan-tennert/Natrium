package de.jan.natrium.interactions

import de.jan.natrium.TypeSafeBuilder
import de.jan.natrium.events.on
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle

fun RowBuilder.of(style: ButtonStyle, id: String, label: String? = null, emoji: Emoji? = null, disabled: Boolean = false, action: suspend (ButtonClickEvent) -> Unit = {}) {
    val button = if(label != null) Button.of(style, id, label).withEmoji(emoji).withDisabled(disabled) else if (emoji != null) Button.of(style, id, emoji).withDisabled(disabled) else throw IllegalStateException("There has to be a label or an emoji in a button")
    components += button
    jda?.on(predicate = { e -> e.componentId == id}, action)
}

fun RowBuilder.of(button: Button, action: suspend (ButtonClickEvent) -> Unit = {}) {
    components += button
    jda?.on(predicate = { e -> e.componentId == button.id}, action)
}

@TypeSafeBuilder
fun RowBuilder.dangerButton(id: String, label: String? = null, emoji: Emoji? = null, disabled: Boolean = false, action: suspend (ButtonClickEvent) -> Unit = {}) = of(
    ButtonStyle.DANGER, id, label, emoji, disabled, action)

@TypeSafeBuilder
fun RowBuilder.successButton(id: String, label: String? = null, emoji: Emoji? = null, disabled: Boolean = false, action: suspend (ButtonClickEvent) -> Unit = {}) = of(
    ButtonStyle.SUCCESS, id, label, emoji, disabled, action)

@TypeSafeBuilder
fun RowBuilder.secondaryButton(id: String, label: String? = null, emoji: Emoji? = null, disabled: Boolean = false, action: suspend (ButtonClickEvent) -> Unit = {}) = of(
    ButtonStyle.SECONDARY, id, label, emoji, disabled, action)

@TypeSafeBuilder
fun RowBuilder.primaryButton(id: String, label: String? = null, emoji: Emoji? = null, disabled: Boolean = false, action: suspend (ButtonClickEvent) -> Unit = {}) = of(
    ButtonStyle.PRIMARY, id, label, emoji, disabled, action)

@TypeSafeBuilder
fun RowBuilder.linkButton(url: String, label: String? = null, emoji: Emoji? = null, disabled: Boolean = false) {
    val button = if(label != null) Button.link(url, label).withEmoji(emoji).withDisabled(disabled) else if (emoji != null) Button.link(url, emoji).withDisabled(disabled) else throw IllegalStateException("There has to be a label or an emoji in a button")
    components += button
}