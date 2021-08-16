package de.jan.natrium.commands.builders

import de.jan.natrium.TypeSafeBuilder
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData

fun buildSubCommandGroups(builder: SubCommandGroupsBuilder.() -> Unit) = SubCommandGroupsBuilder().apply(builder).subCommandGroups.toList()

class SubCommandGroupsBuilder {

    internal val subCommandGroups = mutableListOf<SubcommandGroupData>()

    @TypeSafeBuilder
    fun subCommandGroup(name: String, description: String, builder: SubCommandBuilder.() -> Unit = {}) {
        val subCommands = SubCommandBuilder().apply(builder).subCommands
        val subCommandGroupData = SubcommandGroupData(name, description).apply {
            addSubcommands(subCommands)
        }
        subCommandGroups += subCommandGroupData
    }

}