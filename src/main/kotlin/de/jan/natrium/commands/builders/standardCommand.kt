package de.jan.natrium.commands.builders

import de.jan.natrium.TypeSafeBuilder
import de.jan.natrium.commands.StandardCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message

class StandardCommandImpl(var name: String? = null, var description: String? = null, var syntax: String? = null) {

    @PublishedApi
    internal var action: Action = Action { _, _ ->  }
    var userPermissions = mutableListOf<Permission>()

    var botPermissions = mutableListOf<Permission>()

    var guildOnlyIds = mutableListOf<Long>()

    fun action(action: Action) {
        this.action = action
    }

    fun interface Action {

        suspend fun action(msg: Message, args: List<String>)

    }

}

inline fun standardCommand(standardCommand: StandardCommandImpl.() -> Unit) : StandardCommand {
    val impl = StandardCommandImpl().apply(standardCommand)

    return object : StandardCommand {

        override suspend fun run(msg: Message, args: List<String>) {
            impl.action.action(msg, args)
        }

        override var name = impl.name ?: throw NullPointerException("Name cannot be null")
        override var syntax = impl.syntax ?: ""
        override var description = impl.description ?: throw NullPointerException("Description cannot be null")
        override val guildOnlyIds = impl.guildOnlyIds.toList()
        override val requiredBotPermissions = impl.botPermissions.toList()
        override val requiredUserPermissions = impl.userPermissions.toList()

    }
}