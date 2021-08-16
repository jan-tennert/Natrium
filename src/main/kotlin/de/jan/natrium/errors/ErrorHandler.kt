package de.jan.natrium.errors

import de.jan.natrium.TypeSafeBuilder
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
    open fun onError(exception: Exception, user: User, origin: CommandOrigin) = exception.printStackTrace()

}

class ErrorHandlerImpl {

    internal var onError: (Exception, User, CommandOrigin) -> Unit = {_, _ , _ ->}
    internal var onMissingBotPermission: (User, Permission, CommandOrigin) -> Unit = {_, _ , _ ->}
    internal var onMissingUserPermission: (User, Permission, CommandOrigin) -> Unit = {_, _, _ ->}

    fun onError(action: (Exception, User, CommandOrigin) -> Unit) {
        onError = action
    }

    fun onMissingUserPermission(action: (User, Permission, CommandOrigin) -> Unit) {
        onMissingUserPermission = action
    }

    fun onMissingBotPermission(action: (User, Permission, CommandOrigin) -> Unit) {
        onMissingBotPermission = action
    }

    @PublishedApi
    internal fun build() = object : ErrorHandler() {
        override fun onError(exception: Exception, user: User, origin: CommandOrigin) = this@ErrorHandlerImpl.onError(exception, user, origin)
        override fun onMissingBotPermission(user: User, permission: Permission, origin: CommandOrigin) = this@ErrorHandlerImpl.onMissingBotPermission(user, permission, origin)
        override fun onMissingUserPermission(user: User, permission: Permission, origin: CommandOrigin) = this@ErrorHandlerImpl.onMissingUserPermission(user, permission, origin)
    }

}

inline fun ErrorHandler(builder: ErrorHandlerImpl.() -> Unit) = ErrorHandlerImpl().apply(builder).build()