package de.jan.natrium.paginator

import de.jan.natrium.interactions.RowBuilder
import de.jan.natrium.interactions.of
import de.jan.natrium.messagebuilder.KMessageBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.Interaction
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.dv8tion.jda.api.interactions.components.Component

class ButtonPaginator private constructor(
    private val jda: JDA,
    private var listener: ButtonPaginatorListener = ButtonPaginatorListener { _, _ ->  },
    var maxPage: Int = 20) {

    var nextButton = PaginatorButton( emoji = Emoji.fromUnicode("➡️"))
    var previousButton = PaginatorButton(emoji = Emoji.fromUnicode("⬅️"))
    var lastButton = PaginatorButton( "Last")
    var firstButton = PaginatorButton("First")
    private val buttonsAfter = mutableListOf<Component>()
    private val buttonsBefore = mutableListOf<Component>()
    val allowedUsers = mutableListOf<Long>()
    private var page = 1

    internal fun build() : Message {
        val currentBuilder = KMessageBuilder(jda)
        listener.onUpdate(currentBuilder, page)
        currentBuilder.addButtons()
        return currentBuilder.build()
    }

    fun beforePaginatorButtons(builder: RowBuilder.() -> Unit) {
        buttonsBefore += RowBuilder(jda).apply(builder).components
    }

    fun afterPaginatorButtons(builder: RowBuilder.() -> Unit) {
        buttonsAfter += RowBuilder(jda).apply(builder).components
    }

    private fun update(event: ButtonClickEvent, clicked: ButtonType) {
        val newBuilder = KMessageBuilder(jda)

        when(clicked) {
            ButtonType.FIRST -> page = 1
            ButtonType.NEXT -> page++
            ButtonType.PREVIOUS -> page--
            ButtonType.LAST -> page = maxPage
        }

        listener.onUpdate(newBuilder, page)

        newBuilder.addButtons()

        event.editMessage(newBuilder.build())
            .queue()
    }

    fun onPageChanged(listener: ButtonPaginatorListener) {
        this.listener = listener
    }

    private fun KMessageBuilder.addButtons() {
        addActionRow(jda) {
            for (component in buttonsBefore) {
                of(component as Button)
            }
            of(firstButton.style, generateId(), firstButton.label, firstButton.emoji, disabled = page == 1) { checkUsers(it) {update(it, ButtonType.FIRST) } }
            of(previousButton.style, generateId(), previousButton.label, previousButton.emoji, disabled = page == 1) { checkUsers(it) {update(it, ButtonType.PREVIOUS) } }
            of(nextButton.style, generateId(), nextButton.label, nextButton.emoji, disabled = page == maxPage) { checkUsers(it) {update(it, ButtonType.NEXT) } }
            of(lastButton.style, generateId(), lastButton.label, lastButton.emoji, disabled = page == maxPage) { checkUsers(it) {update(it, ButtonType.LAST) } }
            for (component in buttonsAfter) {
                of(component as Button)
            }
        }
    }

    private fun checkUsers(event: ButtonClickEvent, whenNot: () -> Unit) {
        if(allowedUsers.isNotEmpty() && event.user.idLong !in allowedUsers) return event.deferEdit().queue()
        whenNot()
    }

    enum class ButtonType {
        FIRST,
        NEXT,
        PREVIOUS,
        LAST
    }

    companion object {
        fun MessageChannel.createPaginator(builder: ButtonPaginator.() -> Unit) = ButtonPaginator(jda).apply(builder).build()
        fun Interaction.createPaginator(builder: ButtonPaginator.() -> Unit) = ButtonPaginator(user.jda).apply(builder).build()
        fun create(jda: JDA, builder: ButtonPaginator.() -> Unit) = ButtonPaginator(jda).apply(builder).build()
    }

}

data class PaginatorButton(val label: String? = null, val emoji: Emoji? = null, val style: ButtonStyle = ButtonStyle.PRIMARY)

fun generateId(maxLength: Int = 20) : String {
    val chars = ('A'..'Z').toList() + ('a'..'z').toList()
    var id = ""
    while(id.length != maxLength) id += chars.random()
    return id
}