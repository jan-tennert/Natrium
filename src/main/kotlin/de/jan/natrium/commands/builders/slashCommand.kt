package de.jan.natrium.commands.builders

import de.jan.natrium.TypeSafeBuilder
import de.jan.natrium.commands.SlashCommand
import de.jan.natrium.commands.builders.SlashCommandImpl.Action
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData

class SlashCommandImpl(var name: String? = null, var description: String? = null, var syntax: String? = null) {

    @PublishedApi
    internal val options = mutableListOf<OptionData>()
    @PublishedApi
    internal val subCommands = mutableListOf<SubcommandData>()
    @PublishedApi
    internal val subCommandGroups = mutableListOf<SubcommandGroupData>()

    var userPermissions = mutableListOf<Permission>()

    var botPermissions = mutableListOf<Permission>()

    var guildOnlyIds = mutableListOf<Long>()

    @PublishedApi
    internal var action: Action = Action {  }

    fun action(action: Action) {
        this.action = action
    }

    @TypeSafeBuilder
    fun options(builder: OptionsBuilder.() -> Unit) {
        options += OptionsBuilder().apply(builder).options
    }

    @TypeSafeBuilder
    fun subCommands(builder: SubCommandBuilder.() -> Unit) {
        subCommands += SubCommandBuilder().apply(builder).subCommands
    }

    @TypeSafeBuilder
    fun subCommandGroups(builder: SubCommandGroupsBuilder.() -> Unit) {
        subCommandGroups += SubCommandGroupsBuilder().apply(builder).subCommandGroups
    }

    fun interface Action {

        suspend fun action(event: SlashCommandEvent)

    }

}

inline fun slashCommand(standardCommand: SlashCommandImpl.() -> Unit) : SlashCommand {
    val impl = SlashCommandImpl().apply(standardCommand)

    return object : SlashCommand {

        override suspend fun run(event: SlashCommandEvent) {
            impl.action.action(event)
        }

        override var name = impl.name ?: throw NullPointerException("Name cannot be null")
        override var syntax = impl.syntax ?: ""
        override var description = impl.description ?: throw NullPointerException("Description cannot be null")
        override val subCommands = impl.subCommands.ifEmpty { listOf() }
        override val subCommandGroups = impl.subCommandGroups.ifEmpty { listOf() }
        override val options = impl.options.ifEmpty { listOf() }
        override val guildOnlyIds: List<Long> = impl.guildOnlyIds
        override val requiredBotPermissions = impl.botPermissions.toList()
        override val requiredUserPermissions = impl.userPermissions.toList()

    }
}