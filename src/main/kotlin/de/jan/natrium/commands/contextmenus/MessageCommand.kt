package de.jan.natrium.commands.contextmenus

import net.dv8tion.jda.api.events.interaction.commands.MessageContextCommandEvent
import net.dv8tion.jda.api.interactions.commands.CommandType

abstract class MessageCommand : ContextMenuCommand {

    final override val type = CommandType.MESSAGE_CONTEXT

    abstract fun run(event: MessageContextCommandEvent)

}