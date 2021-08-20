package de.jan.natrium.utils

import de.jan.natrium.commands.GuildManager
import de.jan.natrium.commands.language
import de.jan.translatable.Component
import de.jan.translatable.Translator
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.interactions.Interaction

fun MessageChannel.sendMessage(component: Component, defaultTranslator: Translator? = null) = sendMessage(component.asString(defaultTranslator ?: if(this is GuildChannel && this.guild.language != null) this.guild.language!! else GuildManager.defaultLanguage ?: throw IllegalStateException("No translator available")))

fun Interaction.reply(component: Component, defaultTranslator: Translator? = null) = reply(component.asString(defaultTranslator ?: if(this.guild != null && this.guild!!.language != null) this.guild!!.language!! else GuildManager.defaultLanguage ?: throw IllegalStateException("No translator available")))

fun translatable(key: String, guild: Guild? = null, vararg args: Any) = de.jan.translatable.translatable(key, *args).asString(if(guild != null && guild.language != null) guild.language!! else GuildManager.defaultLanguage ?: throw IllegalStateException("No translator available"))