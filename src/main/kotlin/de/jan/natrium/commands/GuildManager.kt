package de.jan.natrium.commands

import de.jan.translatable.Translator
import net.dv8tion.jda.api.entities.Guild

internal object GuildManager {

    internal var prefixes = hashMapOf<Long, String>()
    internal var languages = hashMapOf<Long, Translator>()
    var globalPrefix = "!"
    var defaultLanguage: Translator? = null

}

var Guild.commandPrefix: String
    get() = GuildManager.prefixes[idLong] ?: GuildManager.globalPrefix
    set(value) {
        GuildManager.prefixes[idLong] = value
    }

var Guild.language: Translator?
    get() = GuildManager.languages[idLong]
    set(value) {
        GuildManager.languages[idLong] = value!!
    }