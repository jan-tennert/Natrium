package de.jan.natrium.interactions

import de.jan.natrium.TypeSafeBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.Component
import net.dv8tion.jda.api.requests.restaction.MessageAction

class ActionRowBuilder(private val jda: JDA? = null) {

    internal val rows = mutableListOf<ActionRow>()

    @TypeSafeBuilder
    fun row(row: RowBuilder.() -> Unit) {
        rows += ActionRow.of(RowBuilder(jda).apply(row).components)
    }

}

class RowBuilder(internal val jda: JDA?) {

    internal val components = mutableListOf<Component>()

}

fun actionRow(jda: JDA? = null, builder: ActionRowBuilder.() -> Unit) = ActionRowBuilder(jda).apply(builder).rows.toList()

fun MessageAction.setActionRows(builder: ActionRowBuilder.() -> Unit) = setActionRows(actionRow(jda, builder))

fun MessageAction.addActionRow(builder: RowBuilder.() -> Unit) = setActionRow(RowBuilder(jda).apply(builder).components)

fun MessageBuilder.setActionRows(jda: JDA? = null, builder: ActionRowBuilder.() -> Unit) = actionRow(jda, builder)
