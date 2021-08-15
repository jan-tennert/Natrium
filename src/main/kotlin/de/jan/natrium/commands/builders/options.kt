package de.jan.natrium.commands.builders

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

inline fun buildOptions(builder: OptionsBuilder.() -> Unit) = OptionsBuilder().apply(builder).options.toList()

class OptionsBuilder {

    @PublishedApi
    internal val options = mutableListOf<OptionData>()

    @OptionBuilder
    fun int(name: String, description: String, required: Boolean = true) {
        options.add(OptionData(OptionType.INTEGER, name, description, required))
    }

    @OptionBuilder
    fun string(name: String, description: String, required: Boolean = true) {
        options.add(OptionData(OptionType.STRING, name, description, required))
    }

    @OptionBuilder
    fun user(name: String, description: String, required: Boolean = true) {
        options.add(OptionData(OptionType.USER, name, description, required))
    }

    @OptionBuilder
    fun mentionable(name: String, description: String, required: Boolean = true) {
        options.add(OptionData(OptionType.MENTIONABLE, name, description, required))
    }

    @OptionBuilder
    fun channel(name: String, description: String, required: Boolean = true) {
        options.add(OptionData(OptionType.CHANNEL, name, description, required))
    }

    @OptionBuilder
    fun boolean(name: String, description: String, required: Boolean = true) {
        options.add(OptionData(OptionType.BOOLEAN, name, description, required))
    }

    @OptionBuilder
    fun role(name: String, description: String, required: Boolean = true) {
        options.add(OptionData(OptionType.ROLE, name, description, required))
    }

    /*@OptionBuilder
    fun number(name: String, description: String, required: Boolean = true)
    JDA doesn't support that currently*/

}

@DslMarker
annotation class OptionBuilder