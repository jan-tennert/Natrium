package de.jan.natrium.commands

import de.jan.natrium.errors.ErrorHandler
import de.jan.natrium.errors.ErrorHandlerImpl
import de.jan.natrium.events.on
import de.jan.natrium.scope
import de.jan.natrium.utils.schedule
import de.jan.translatable.Translator
import de.jan.translatable.TranslatorManager
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.ConcurrentHashMap

class CommandHandler internal constructor(val jda: JDA) {

    private val slashCommands = mutableListOf<SlashCommand>()
    private val standardCommands = mutableListOf<StandardCommand>()
    private val hybridCommands = mutableListOf<HybridCommand>()
    private val timeoutUsers = ConcurrentHashMap<Long, LocalDateTime>()
    var defaultTimeout: Duration = Duration.ZERO
    val commands: List<Command>
        get() = slashCommands + standardCommands + hybridCommands
    var translationManager: TranslatorManager? = null
    var allowMentionPrefix: Boolean = true //TODO
    var errorHandler = ErrorHandler()

    init {
        initSlashCommandHandler()
        initStandardCommandHandler()
    }

    /**
     * You can use this method if you store the prefixes in a database
     */
    fun loadPrefixes(init: HashMap<Long, String>.() -> Unit) {
        GuildManager.prefixes = hashMapOf<Long, String>().apply(init)
    }

    /**
     * You can use this method if you store the languages in a database
     */
    fun loadLanguages(init: HashMap<Long, Translator>.() -> Unit) {
        GuildManager.languages = hashMapOf<Long, Translator>().apply(init)
    }

    fun setErrorHandler(errorHandlerImpl: ErrorHandlerImpl.() -> Unit) {
        errorHandler = ErrorHandlerImpl().apply(errorHandlerImpl).build()
    }

    operator fun plusAssign(command: Command) = register(command)

    operator fun plusAssign(commands: List<Command>) = register(commands)

    fun register(command: Command) {
        when(command) {
            is StandardCommand -> standardCommands.add(command)
            is SlashCommand -> slashCommands.add(command)
            is HybridCommand -> hybridCommands.add(command)
            else -> throw UnsupportedOperationException()
        }
    }

    fun register(commands: List<Command>) = commands.forEach { register(it) }

    fun register(vararg commands: Command) = register(commands.toList())

    /**
     * Updates all slash commands. You may need to call the [JDA.awaitReady] method to ensure that all guilds are loaded
     */
    fun finish(): CommandHandler {
        val cmds = slashCommands + hybridCommands
        val globalCommands = jda.updateCommands()
        val guildCommands = hashMapOf<Long, CommandListUpdateAction>()
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
                        if(cmd.autoAcknowledge) deferReply().queue()

                        //Check permissions
                        if(checkBotPermission(isFromGuild, cmd, user, guild?.selfMember, CommandOrigin(interaction = interaction), this@on)) return@launch
                        if(checkPermission(isFromGuild, cmd, member, CommandOrigin(interaction = interaction), this@on)) return@launch

                        if(cmd is HybridCommand) {
                            val args = mutableListOf<String>()

                            //Convert slash command options to normal arguments
                            if(subcommandGroup != null) args += subcommandGroup!!
                            if(subcommandName != null) args += subcommandName!!
                            for (option in options) {
                                args += option.asString
                            }

                            catchError(user, CommandOrigin(interaction = interaction), this@on) {
                                cmd.run(CommandResult(channel, args, user, member, CommandOrigin(interaction = interaction)))
                            }
                        } else if(cmd is SlashCommand) {
                            catchError(user, CommandOrigin(interaction = interaction), this@on) {
                                cmd.run(this@on)
                            }
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
                        //Check permissions
                        if(checkBotPermission(isFromGuild, cmd, author, if(isFromGuild) guild.selfMember else null, CommandOrigin(channel))) return@launch
                        if(checkPermission(isFromGuild, cmd, member, CommandOrigin(channel))) return@launch

                        //Check if the user can't run the command because the timeout isn't over
                        if(timeoutUsers.containsKey(author.idLong)) {
                            val remaining = (cmd as AbstractCommand).timeout - Duration.ofMillis(ChronoUnit.MILLIS.between(timeoutUsers[author.idLong], LocalDateTime.now()))
                            errorHandler.onCommandTimeout(author, CommandOrigin(channel), remaining)
                            return@launch
                        }

                        if(cmd is HybridCommand) {
                            catchError(user = author, CommandOrigin(channel)) {
                                cmd.run(CommandResult(channel, args, author, member, CommandOrigin(channel)))
                            }
                        } else if(cmd is StandardCommand) {
                            catchError(user = author, CommandOrigin(channel)) {
                                cmd.run(message, args)
                            }
                        }

                        //Add timeout, if available
                        if(!cmd.timeout.isZero || !defaultTimeout.isZero) {
                            timeoutUsers[author.idLong] = LocalDateTime.now()
                            launch {
                                val timeout = if(cmd.timeout.isZero) defaultTimeout else cmd.timeout
                                schedule(timeout) {
                                    timeoutUsers.remove(author.idLong)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun catchError(user: User, origin: CommandOrigin, event: SlashCommandEvent? = null, method: suspend () -> Unit) = try {
        method()
    } catch(e: Exception) {
        if(event != null && !event.isAcknowledged) event.deferReply().queue()
        errorHandler.onError(e, user, origin)
    }

    private fun checkPermission(guild: Boolean, cmd: Command, member: Member?, origin: CommandOrigin, event: SlashCommandEvent? = null): Boolean {
        if(cmd.requiredUserPermissions.isNotEmpty() && guild) {
            cmd.requiredUserPermissions.forEach {
                if(!member!!.hasPermission(it)) {
                    if(event != null && !event.isAcknowledged) event.deferReply().queue()
                    errorHandler.onMissingUserPermission(member.user, it, origin)
                    return true
                }
            }
        }
        return false
    }

    private fun checkBotPermission(guild: Boolean, cmd: Command, author: User, selfMember: Member?, origin: CommandOrigin, event: SlashCommandEvent? = null): Boolean {
        if(cmd.requiredBotPermissions.isNotEmpty() && guild) {
            cmd.requiredBotPermissions.forEach {
                if(!selfMember!!.hasPermission(it)) {
                    if(event != null && !event.isAcknowledged) event.deferReply().queue()
                    errorHandler.onMissingBotPermission(author, it, origin)
                    return true
                }
            }
        }
        return false
    }

}