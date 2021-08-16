package de.jan.natrium.errors

import de.jan.natrium.commands.CommandOrigin
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import kotlin.Exception
import de.jan.natrium.commands.Command

open class ErrorHandler {

    /**
     * This method will be run if the user running a command doesn't have the permission
     * @param user The user trying to execute the command
     * @param permission The permission which is missing
     * @param origin The channel/hook. Use this to send a message.
     */
    open fun onMissingUserPermission(user: User, permission: Permission, origin: CommandOrigin) = Unit

    /**
     * This method will be run if the bot doesn't have enough permissions to run this command
     * @param user The user trying to execute the command
     * @param permission The permission which is missing
     * @param origin The channel/hook. Use this to send a message.
     */
    open fun onMissingBotPermission(user: User, permission: Permission, origin: CommandOrigin) = Unit

    /**
     * This method will be run if an error is thrown when running a [Command]
     */
    open fun onError(exception: Exception) = exception.printStackTrace()

}