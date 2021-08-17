package de.jan.natrium.commands

import net.dv8tion.jda.api.Permission

sealed interface Command {

    /**
     * This is the command name
     */
    var name: String

    /**
     * This is the command syntax which can be used for things like a help command.
     */
    var syntax: String

    /**
     * This is the command description
     */
    var description: String

    /**
     * If not empty, only these guilds are able to use these commands
     */
    val guildOnlyIds: List<Long>
        get() = listOf()

    /**
     * If not empty, the user running the command has to have this permission for this command
     */
    val requiredUserPermissions: List<Permission>
        get() = listOf()

    /**
     * If not empty, the bot has to have this permission for this command
     */
    val requiredBotPermissions: List<Permission>
        get() = listOf()

}