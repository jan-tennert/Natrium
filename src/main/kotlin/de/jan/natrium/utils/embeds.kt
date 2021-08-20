package de.jan.natrium.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageChannel
import java.time.temporal.TemporalAccessor

fun messageEmbed(embed: EmbedBuilder.() -> Unit) = EmbedBuilder().apply(embed).build()

fun EmbedBuilder.title(title: String, url: String? = null) = setTitle(title, url)

fun EmbedBuilder.author(author: String, url: String? = null, iconUrl: String? = null) = setAuthor(author, url, iconUrl)

fun EmbedBuilder.field(name: String, value: String, inline: Boolean = false) = addField(name, value, inline)

fun EmbedBuilder.blankField(inline: Boolean = false) = addBlankField(inline)

fun EmbedBuilder.thumbnail(url: String) = setThumbnail(url)

fun EmbedBuilder.image(url: String) = setImage(url)

fun EmbedBuilder.footer(footer: String, iconUrl: String? = null) = setFooter(footer, iconUrl)

fun EmbedBuilder.timestamp(timestamp: TemporalAccessor) = setTimestamp(timestamp)

fun MessageChannel.sendMessageEmbed(embed: EmbedBuilder.() -> Unit) = sendMessageEmbeds(messageEmbed(embed))
