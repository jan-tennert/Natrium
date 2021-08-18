package de.jan.natrium.commands.contextmenus

import de.jan.natrium.commands.Command
import net.dv8tion.jda.api.interactions.commands.CommandType
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface ContextMenuCommand : Command {

    val type: CommandType

    override fun build() = CommandData(type, name)

}