package de.jan.natrium.events

import kotlinx.coroutines.suspendCancellableCoroutine
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.concurrent.Task
import java.util.concurrent.CompletableFuture

inline fun <reified E : GenericEvent> JDA.on(crossinline predicate: (E) -> Boolean = { true }, crossinline onEvent: suspend E.() -> Unit): CoroutineHandler {
    val handler = CoroutineHandler {
        if(it is E && predicate(it)) {
            onEvent(it)
        }
    }
    addEventListener(handler)
    return handler
}

suspend inline fun <reified E: GenericEvent> JDA.awaitEvent(crossinline filter: (E) -> Boolean = { true }) = suspendCancellableCoroutine<E> {
    val handler = object : CoroutineHandler {
        override suspend fun handle(event: GenericEvent) {
            if(event is E && filter(event)) {
                it.resume(event) { error -> error.printStackTrace() }
                removeEventListener(this)
            }
        }
    }
    it.invokeOnCancellation { removeEventListener(handler) }
}

suspend inline fun <A>RestAction<A>.await() = suspendCancellableCoroutine<A> {
    queue { result ->
        it.resume(result) { it.printStackTrace() }
    }
}

suspend fun <T> Task<T>.await() = suspendCancellableCoroutine<T> {
    onSuccess { re -> it.resume(re) { it.printStackTrace() } }
}

suspend fun <T> CompletableFuture<T>.await() = suspendCancellableCoroutine<T> {
    whenComplete { t, u ->
        it.resume(t) { it.printStackTrace() }
    }
}