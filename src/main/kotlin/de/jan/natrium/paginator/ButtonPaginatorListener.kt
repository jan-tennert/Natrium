package de.jan.natrium.paginator

import de.jan.natrium.messagebuilder.KMessageBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.requests.restaction.MessageAction

fun interface ButtonPaginatorListener {

    /**
     * Called when the page of the paginator is changed
     */
    fun onUpdate(builder: KMessageBuilder, page: Int)

}