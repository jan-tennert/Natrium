package de.jan.natrium.commands

import de.jan.natrium.events.on
import de.jan.natrium.scope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction

class CommandHandler internal constructor(val jda: JDA) {

    private val slashCommands = mutableListOf<SlashCommand>()
    private val standardCommands = mutableListOf<StandardCommand>()
    private val hybridCommands = mutableListOf<HybridCommand>()
    val commands: List<Command>
        get() = slashCommands + standardCommands + hybridCommands

    var globalPrefix: String = PrefixManager.globalPrefix
        set(value) {
            PrefixManager.globalPrefix = field
            field = value
        }
    var allowMentionPrefix: Boolean = true //TODO

    init {
        initSlashCommandHandler()
        initStandardCommandHandler()
    }

    fun loadPrefixes(init: HashMap<String, String>.() -> Unit) {
        PrefixManager.prefixes = hashMapOf<String, String>().apply(init)
    }

    operator fun plusAssign(slashCommand: SlashCommand) = register(slashCommand)

    operator fun plusAssign(hybridCommand: HybridCommand) = register(hybridCommand)

    operator fun plusAssign(standardCommand: StandardCommand) = register(standardCommand)

    fun register(slashCommand: SlashCommand) {
        slashCommands.add(slashCommand)
    }

    fun register(hybridCommand: HybridCommand) {
        hybridCommands.add(hybridCommand)
    }

    fun register(standardCommand: StandardCommand) {
        standardCommands.add(standardCommand)
    }

    /**
     * Updates all slash commands. You may need to call the [JDA.awaitReady] method to ensure that all guilds are loaded
     */
    fun finish(): CommandHandler {
        val cmds = slashCommands + hybridCommands
        val globalCommands = jda.updateCommands()
        val guildCommands = hashMapOf<String, CommandListUpdateAction>()
        for (cmd in cmds) {
            if(!cmd.autoRegister) continue
            if(cmd.guildOnlyIds.isNotEmpty()) {
                for (id in cmd.guildOnlyIds) {
                    if(id in guildCommands.keys) {
                        guildCommands[id]!!.addCommands(cmd.build())
                    } else {
                        val guild = jda.getGuildById(id) ?: continue
                        val update = guild.updateCommands()
                        update.addCommands(cmd.build())
                        guildCommands[id] = update
                    }
                }
            } else {
                globalCommands.addCommands(cmd.build())
            }
        }

        //Now send the commands
        globalCommands.queue()
        for ((_, update) in guildCommands) {
            update.queue()
        }
        return this
    }

    private fun initSlashCommandHandler() =
        jda.on<SlashCommandEvent> {
            val commands = slashCommands + hybridCommands
            for (cmd in commands) {
                if (cmd.name == name) {
                    jda.scope.launch {
                        if(cmd is HybridCommand) {
                            if(cmd.autoAcknowledge) deferReply().queue()
                            val args = mutableListOf<String>()
                            if(subcommandGroup != null) args += subcommandGroup!!
                            if(subcommandName != null) args += subcommandName!!
                            for (option in options) {
                                args += option.asString
                            }
                            cmd.run(CommandResult(channel, args, user, member, CommandOrigin(hook = hook)))
                        } else if(cmd is SlashCommand) {
                            cmd.run(this@on)
                        }
                    }
                }
            }
        }

    private fun initStandardCommandHandler() = jda.on<MessageReceivedEvent>() {
        if (message.contentRaw.startsWith(guild.commandPrefix)) {
            val command = message.contentRaw.substring(1).split(" ")[0]
            val prefix = guild.commandPrefix
            val commands = standardCommands + hybridCommands
            var msg = message.contentRaw.replace("$prefix$command", "")
            if (msg.startsWith(" ")) {
                msg = msg.substring(1)
            }
            val args = msg.split(" ")
            for (cmd in commands) {
                if (cmd.name == command) {
                    jda.scope.launch {
                        if(cmd is HybridCommand) {
                            cmd.run(CommandResult(channel, args, author, member, CommandOrigin(channel)))
                        } else if(cmd is StandardCommand) {
                            cmd.run(message, args, guild.commandPrefix)
                        }
                    }
                }
            }
        }
    }

}