package de.jan.natrium.commands.builders

import de.jan.natrium.commands.contextmenus.MessageCommand
import de.jan.natrium.commands.contextmenus.UserCommand
import net.dv8tion.jda.api.events.interaction.commands.MessageContextCommandEvent
import net.dv8tion.jda.api.events.interaction.commands.UserContextCommandEvent

class MessageCommandImpl(var name: String? = null, var description: String? = null) {

    var guildOnlyIds = mutableListOf<Long>()
    private var action = Action {}

    fun action(action: Action) {
        this.action = action
    }

    fun build() = object : MessageCommand() {
        override fun run(event: MessageContextCommandEvent) {
            action.action(event)
        }
        override var name = this@MessageCommandImpl.name ?: throw NullPointerException("Name cannot be null")
    }

    fun interface Action {

        fun action(event: MessageContextCommandEvent)

    }

}

class UserCommandImpl(var name: String? = null, var description: String? = null) {

    var guildOnlyIds = mutableListOf<Long>()
    private var action = Action {}

    fun action(action: Action) {
        this.action = action
    }

    fun build() = object : UserCommand() {
        override fun run(event: UserContextCommandEvent) {
            action.action(event)
        }
        override var name = this@UserCommandImpl.name ?: throw NullPointerException("Name cannot be null")
    }

    fun interface Action {

        fun action(event: UserContextCommandEvent)

    }

}

fun userCommand(builder: UserCommandImpl.() -> Unit) = UserCommandImpl().apply(builder).build()
fun messageCommand(builder: MessageCommandImpl.() -> Unit) = MessageCommandImpl().apply(builder).build()


