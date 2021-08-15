package de.jan.natrium.events

import de.jan.natrium.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.IEventManager

class EventHandler(val jda: JDA) : IEventManager {

    private val listeners = mutableListOf<Any>()

    override fun register(listener: Any) {
        if (listener is CoroutineHandler || listener is EventListener) {
            listeners.add(listener)
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun unregister(listener: Any) {
        if (listeners.contains(listener)) {
            listeners.remove(listener)
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun handle(event: GenericEvent) {
        for (listener in listeners) {
            jda.launch {
                when (listener) {
                    is CoroutineHandler -> listener.handle(event)
                    is EventListener -> listener.onEvent(event)
                    else -> throw UnsupportedOperationException()
                }
            }
        }
    }

    override fun getRegisteredListeners() = listeners
}

fun interface CoroutineHandler {

    suspend fun handle(event: GenericEvent)

}