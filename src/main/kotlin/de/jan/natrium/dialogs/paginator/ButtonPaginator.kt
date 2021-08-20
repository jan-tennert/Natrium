package de.jan.natrium.dialogs.paginator

import de.jan.natrium.dialogs.ButtonDialog
import de.jan.natrium.dialogs.DialogButton
import de.jan.natrium.interactions.of
import de.jan.natrium.messagebuilder.KMessageBuilder
import de.jan.natrium.messagebuilder.buildMessage
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.Interaction

class ButtonPaginator private constructor(
    override val jda: JDA,
    private var listener: ButtonPaginatorListener = ButtonPaginatorListener { _, _ ->  },
    var maxPage: Int = 20) : ButtonDialog {

    var nextButton = DialogButton( emoji = Emoji.fromUnicode("➡️"))
    var previousButton = DialogButton(emoji = Emoji.fromUnicode("⬅️"))
    var lastButton = DialogButton( "Last")
    var firstButton = DialogButton("First")
    val allowedUsers = mutableListOf<Long>()
    private var page = 1
    override var init: (KMessageBuilder) -> Unit
        get() = throw UnsupportedOperationException()
        set(value) { throw UnsupportedOperationException()}

    fun build() = buildMessage(jda) {
        listener.onUpdate(this, page)
        addButtons()
    }

    private fun update(event: ButtonClickEvent, clicked: ButtonType) = event.editMessage(buildMessage(jda) {
        when (clicked) {
            ButtonType.FIRST -> page = 1
            ButtonType.NEXT -> page++
            ButtonType.PREVIOUS -> page--
            ButtonType.LAST -> page = maxPage
        }

        listener.onUpdate(this, page)

        addButtons()
    })
        .queue()

    fun onPageChanged(listener: ButtonPaginatorListener) {
        this.listener = listener
    }

    private fun KMessageBuilder.addButtons() {
        addActionRow(jda) {
            buttonsBefore.forEach { of(it) }
            of(firstButton.style, generateId(), firstButton.label, firstButton.emoji, disabled = page == 1) { checkUsers(it) {update(it,
                ButtonType.FIRST
            ) } }
            of(previousButton.style, generateId(), previousButton.label, previousButton.emoji, disabled = page == 1) { checkUsers(it) {update(it,
                ButtonType.PREVIOUS
            ) } }
            of(nextButton.style, generateId(), nextButton.label, nextButton.emoji, disabled = page == maxPage) { checkUsers(it) {update(it,
                ButtonType.NEXT
            ) } }
            of(lastButton.style, generateId(), lastButton.label, lastButton.emoji, disabled = page == maxPage) { checkUsers(it) {update(it,
                ButtonType.LAST
            ) } }
            buttonsAfter.forEach { of(it) }
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


fun generateId(maxLength: Int = 20) : String {
    val chars = ('A'..'Z').toList() + ('a'..'z').toList()
    var id = ""
    while(id.length != maxLength) id += chars.random()
    return id
}