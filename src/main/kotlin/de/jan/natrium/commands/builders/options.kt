package de.jan.natrium.commands.builders

import de.jan.natrium.TypeSafeBuilder
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

inline fun buildOptions(builder: OptionsBuilder.() -> Unit) = OptionsBuilder().apply(builder).options.toList()

class OptionsBuilder {

    @PublishedApi
    internal val options = mutableListOf<OptionData>()

    @TypeSafeBuilder
    fun int(name: String, description: String, required: Boolean = true, choices: ChoiceBuilder<Int>.() -> Unit = {}) {
        val option = OptionData(OptionType.INTEGER, name, description, required)
        val choiceBuilder = ChoiceBuilder<Int>().apply(choices)
        if(choiceBuilder.choices.isNotEmpty()) option.addChoices(choiceBuilder.choices)
        options += option
    }

    @TypeSafeBuilder
    fun string(name: String, description: String, required: Boolean = true, choices: ChoiceBuilder<String>.() -> Unit = {}) {
        val option = OptionData(OptionType.STRING, name, description, required)
        val choiceBuilder = ChoiceBuilder<String>().apply(choices)
        if(choiceBuilder.choices.isNotEmpty()) option.addChoices(choiceBuilder.choices)
        options += option
    }

    @TypeSafeBuilder
    fun user(name: String, description: String, required: Boolean = true) {
        options += OptionData(OptionType.USER, name, description, required)
    }

    @TypeSafeBuilder
    fun mentionable(name: String, description: String, required: Boolean = true) {
        options += OptionData(OptionType.MENTIONABLE, name, description, required)
    }

    @TypeSafeBuilder
    fun channel(name: String, description: String, required: Boolean = true) {
        options += OptionData(OptionType.CHANNEL, name, description, required)
    }

    @TypeSafeBuilder
    fun boolean(name: String, description: String, required: Boolean = true) {
        options += OptionData(OptionType.BOOLEAN, name, description, required)
    }

    @TypeSafeBuilder
    fun role(name: String, description: String, required: Boolean = true) {
        options += OptionData(OptionType.ROLE, name, description, required)
    }

    /* TODO: JDA doesn't support that right now
    @TypeSafeBuilder
    fun number(name: String, description: String, required: Boolean = true, choices: ChoiceBuilder<Double>.() -> Unit = {}) {
        val option = OptionData(OptionType.NUMBER, name, description, required)
        val choiceBuilder = ChoiceBuilder<Double>().apply(choices)
        if(choiceBuilder.choices.isNotEmpty()) option.addChoices(choiceBuilder.choices)
        options += option
    }
    */

}

class ChoiceBuilder<V> {

    internal val choices = mutableListOf<Command.Choice>()

    @TypeSafeBuilder
    fun choice(name: String, value: V) {
        if(value is Long) {
            choices += Command.Choice(name, value)
            return
        }
        choices += Command.Choice(name, value.toString())
    }

}