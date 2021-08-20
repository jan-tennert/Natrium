package de.jan.natrium.dialogs

import de.jan.natrium.interactions.RowBuilder
import de.jan.natrium.messagebuilder.KMessageBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle

interface ButtonDialog {

    val jda: JDA
    val buttonsAfter: MutableList<Button>
        get() = mutableListOf<Button>()
    val buttonsBefore: MutableList<Button>
        get() = mutableListOf<Button>()
    var init: (KMessageBuilder) -> Unit

    fun init(init: KMessageBuilder.() -> Unit) {
        this.init = init
    }

    fun beforeStandardButtons(builder: RowBuilder.() -> Unit) {
        buttonsBefore += RowBuilder(jda).apply(builder).components.filterIsInstance<Button>()
    }

    fun afterStandardButtons(builder: RowBuilder.() -> Unit) {
        buttonsAfter += RowBuilder(jda).apply(builder).components.filterIsInstance<Button>()
    }

}

data class DialogButton(val label: String? = null, val emoji: Emoji? = null, val style: ButtonStyle = ButtonStyle.PRIMARY)
