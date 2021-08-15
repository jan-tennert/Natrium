package de.jan.natrium.commands

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData

interface ISlashCommand : Command {

    val options: List<OptionData>
        get() = listOf()
    val subCommands: List<SubcommandData>
        get() = listOf()
    val subCommandGroups: List<SubcommandGroupData>
        get() = listOf()
    val guildOnlyIds: List<String>
        get() = listOf()
    val autoRegister: Boolean
        get() = true

    fun build() = CommandData(name, description).apply {
        if(this@ISlashCommand.subCommandGroups.isNotEmpty()) addSubcommandGroups(this@ISlashCommand.subCommandGroups)
        if(this@ISlashCommand.subCommands.isNotEmpty()) addSubcommands(this@ISlashCommand.subCommands)
        if(this@ISlashCommand.options.isNotEmpty()) addOptions(this@ISlashCommand.options)
    }

}

interface SlashCommand : ISlashCommand {

    suspend fun run(event: SlashCommandEvent)

}