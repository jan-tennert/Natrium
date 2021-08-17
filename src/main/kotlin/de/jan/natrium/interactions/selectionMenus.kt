package de.jan.natrium.interactions

import de.jan.natrium.TypeSafeBuilder
import de.jan.natrium.events.on
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu

fun RowBuilder.selectionMenu(id: String, disabled: Boolean = false, placeholder: String? = null, minValues: Int = 1, maxValues: Int = 1, range: Pair<Int, Int> = minValues to maxValues, options: SelectionMenuOptionBuilder.() -> Unit = {}, action: suspend (SelectionMenuEvent) -> Unit = {}) {
    val options = SelectionMenuOptionBuilder().apply(options).options.toList()
    val menu = SelectionMenu.create(id)
        .setRequiredRange(range.first, range.second)
        .setPlaceholder(placeholder)
        .setDisabled(disabled)
        .addOptions(options)
    components += menu.build()

    jda?.on(predicate = { it.componentId == id }, action)
}

class SelectionMenuOptionBuilder {

    internal val options = mutableListOf<SelectOption>()

    @TypeSafeBuilder
    fun option(label: String, value: String, emoji: Emoji? = null, description: String? = null, default: Boolean = false) {
        val option = SelectOption.of(label, value)
            .withEmoji(emoji)
            .withDescription(description)
            .withDefault(default)
        options += option
    }

}