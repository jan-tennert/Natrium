package de.jan.natrium.dialogs.confirmation

import de.jan.natrium.dialogs.ButtonDialog
import de.jan.natrium.dialogs.DialogButton
import de.jan.natrium.dialogs.paginator.generateId
import de.jan.natrium.interactions.of
import de.jan.natrium.messagebuilder.KMessageBuilder
import de.jan.natrium.messagebuilder.buildMessage
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.components.ButtonStyle

class ConfirmationDialog private constructor(override val jda: JDA) : ButtonDialog {

    var confirmationButton = DialogButton("Yes", style = ButtonStyle.SUCCESS)
    var cancelButton = DialogButton("Cancel", style = ButtonStyle.DANGER)
    override var init: (KMessageBuilder) -> Unit = {}
    private var onConfirmation: (ButtonClickEvent) -> Unit = {}
    private var onCancel: (ButtonClickEvent) -> Unit = {}

    override fun init(init: KMessageBuilder.() -> Unit) {
        this.init = init
    }

    fun onConfirmation(onConfirmation: (ButtonClickEvent) -> Unit) {
        this.onConfirmation = onConfirmation
    }

    fun onCancel(onCancel: (ButtonClickEvent) -> Unit) {
        this.onCancel = onCancel
    }

    fun build() = buildMessage(jda) {
        init(this)
        addActionRow {
            buttonsBefore.forEach { of(it) }
            of(confirmationButton.style, generateId(), confirmationButton.label, confirmationButton.emoji) { onConfirmation(it) }
            of(cancelButton.style, generateId(), cancelButton.label, cancelButton.emoji) { onCancel(it) }
            buttonsAfter.forEach { of(it) }
        }
    }

    companion object {
        fun create(jda: JDA, builder: ConfirmationDialog.() -> Unit) = ConfirmationDialog(jda).apply(builder).build()
    }
}