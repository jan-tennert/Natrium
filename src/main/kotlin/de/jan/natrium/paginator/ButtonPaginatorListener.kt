package de.jan.natrium.paginator

import de.jan.natrium.messagebuilder.KMessageBuilder

fun interface ButtonPaginatorListener {

    /**
     * Called when the page of the paginator is changed
     */
    fun onUpdate(builder: KMessageBuilder, page: Int)

}