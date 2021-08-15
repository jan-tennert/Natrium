package de.jan.natrium.commands.builders

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

fun buildSubCommands(builder: SubCommandBuilder.() -> Unit) = SubCommandBuilder().apply(builder).subCommands.toList()

class SubCommandBuilder {

    internal val subCommands = mutableListOf<SubcommandData>()

    @OptionBuilder
    fun subCommand(name: String, description: String, builder: OptionsBuilder.() -> Unit = {}) {
        val options = OptionsBuilder().apply(builder).options
        val subcommandData = SubcommandData(name, description)
        if(options.isNotEmpty()) subcommandData.addOptions(options)
        subCommands += subcommandData
    }

}