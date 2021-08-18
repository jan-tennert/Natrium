package de.jan.natrium.commands.contextmenus

import net.dv8tion.jda.api.events.interaction.commands.UserCommandEvent
import net.dv8tion.jda.api.interactions.commands.CommandType

abstract class UserCommand : ContextMenuCommand {

    final override val type = CommandType.USER_COMMAND

    abstract fun run(event: UserCommandEvent)

}