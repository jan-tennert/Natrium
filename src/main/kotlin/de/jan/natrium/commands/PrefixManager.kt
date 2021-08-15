package de.jan.natrium.commands

import net.dv8tion.jda.api.entities.Guild

object PrefixManager {

    internal var prefixes = hashMapOf<String, String>()
    var globalPrefix = "!"

    operator fun set(guild: Guild, prefix: String) = set(guild.id, prefix)

    operator fun set(id: String, prefix: String) {
        prefixes[id] = prefix
    }

    operator fun get(id: String) = prefixes[id] ?: globalPrefix

    operator fun get(guild: Guild) = get(guild.id)

}

var Guild.commandPrefix: String
    get() = PrefixManager[this]
    set(value) {
        PrefixManager[this] = value
    }