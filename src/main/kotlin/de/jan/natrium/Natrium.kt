package de.jan.natrium

import de.jan.natrium.commands.CommandHandler
import de.jan.natrium.commands.CommandResult
import de.jan.natrium.commands.HybridCommand
import de.jan.natrium.commands.builders.buildSubCommandGroups
import de.jan.natrium.events.EventHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder

internal val JDA.scope: CoroutineScope
    get() = CoroutineScope(Dispatchers.IO)

fun JDA.launch(block: suspend CoroutineScope.() -> Unit) = scope.launch(block = block)

fun JDA.enableNatrium() = setEventManager(EventHandler(this)).run { this@enableNatrium }

fun JDA.createCommandHandler() = CommandHandler(this)