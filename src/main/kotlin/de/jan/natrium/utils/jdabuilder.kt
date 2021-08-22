package de.jan.natrium.utils

import com.neovisionaries.ws.client.WebSocketFactory
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.audio.factory.IAudioSendFactory
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.IEventManager
import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.Compression
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.SessionController
import net.dv8tion.jda.api.utils.cache.CacheFlag
import okhttp3.OkHttpClient
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService

class KJDABuilder(val builder: JDABuilder) {

    var memberCachePolicy: MemberCachePolicy? = null
        set(value) { builder.setMemberCachePolicy(value); field = value }

    var contextMap: ConcurrentMap<String, String>? = null
        set(value) { builder.setContextMap(value); field = value }

    var contextEnabled: Boolean = true
        set(value) { builder.setContextEnabled(value); field = value }

    var compression: Compression? = null
        set(value) { value?.let { builder.setCompression(it) }; field = value }

    var gatewayPool: ScheduledExecutorService? = null
        set(value) { builder.setGatewayPool(value, true); field = value }

    var callbackPool: ExecutorService? = null
        set(value) { builder.setCallbackPool(value, true); field = value }

    var requestTimeoutRetry: Boolean = true
        set(value) { builder.setRequestTimeoutRetry(value); field = value }

    var enableShutdownHook: Boolean = true
        set(value) { builder.setEnableShutdownHook(value); field = value }

    var rateLimitPool: ScheduledExecutorService? = null
        set(value) { builder.setRateLimitPool(value, true); field = value }

    var eventPool: ExecutorService? = null
        set(value) { builder.setEventPool(value, true); field = value }

    var autoReconnect: Boolean = true
        set(value) { builder.setAutoReconnect(value); field = value }

    var token: String? = null
        set(value) { builder.setToken(value); field = value }

    var httpClientBuilder: OkHttpClient.Builder? = null
        set(value) { builder.setHttpClientBuilder(value); field = value }

    var httpClient: OkHttpClient? = null
        set(value) { builder.setHttpClient(value); field = value }

    var webSocketFactory: WebSocketFactory? = null
        set(value) { builder.setWebsocketFactory(value); field = value }

    var audioPool: ScheduledExecutorService? = null
        set(value) { builder.setAudioPool(value, true); field = value }

    var bulkDeleteSplittingEnabled: Boolean = true
        set(value) { builder.setBulkDeleteSplittingEnabled(value); field = value }

    var sessionController: SessionController? = null
        set(value) { builder.setSessionController(value); field = value }

    var voiceDispatchInterceptor: VoiceDispatchInterceptor? = null
        set(value) { builder.setVoiceDispatchInterceptor(value); field = value }

    var chunkingFilter: ChunkingFilter? = null
        set(value) { builder.setChunkingFilter(value); field = value }

    var activity: Activity? = null
        set(value) { builder.setActivity(value); field = value }

    var status: OnlineStatus? = null
        set(value) { value?.let { builder.setStatus(it) }; field = value }

    var eventListeners: MutableSet<Any> = mutableSetOf()

    var maxReconnectDelay: Int = 900
        set(value) { builder.setMaxReconnectDelay(value); field = value }

    var eventManager: IEventManager? = null
    set(value) { builder.setEventManager(value); field = value }

    var audioSendFactory: IAudioSendFactory? = null
        set(value) { builder.setAudioSendFactory(value); field = value }

    var idle: Boolean = false
        set(value) { builder.setIdle(value); field = value }

    var largeThreshold: Int = 250
        set(value) { builder.setLargeThreshold(value); field = value }

    var maxBufferSize: Int = 2048
        set(value) { builder.setMaxBufferSize(value); field = value }

    inline fun cacheFlags(init: Builder<CacheFlag>.() -> Unit) {
        val flags = Builder<CacheFlag>().apply(init)
        builder.enableCache(flags.allow)
        builder.disableCache(flags.deny)
    }

    inline fun intents(init: Builder<GatewayIntent>.() -> Unit) {
        val flags = Builder<GatewayIntent>().apply(init)
        builder.enableIntents(flags.allow)
        builder.disableIntents(flags.deny)
    }

    fun useSharding(id: Int, total: Int) = builder.useSharding(id, total)

    @PublishedApi
    internal fun build() = builder.apply {
        addEventListeners(eventListeners)
    }.build()

    class Builder<T> {

        @PublishedApi
        internal val allow = mutableListOf<T>()
        @PublishedApi
        internal val deny = mutableListOf<T>()

        operator fun T.unaryPlus() {
            allow.add(this)
        }

        operator fun T.unaryMinus() {
            deny.add(this)
        }

        operator fun Collection<T>.unaryPlus() {
            allow.addAll(this)
        }

        operator fun Collection<T>.unaryMinus() {
            allow.addAll(this)
        }

        operator fun GatewayIntent.plus(intent: GatewayIntent) = listOf(this, intent)

    }

}

inline fun DefaultJDA(token: String, builder: KJDABuilder.() -> Unit) = KJDABuilder(JDABuilder.createDefault(token)).apply(builder).builder.build()
inline fun LightJDA(token: String, builder: KJDABuilder.() -> Unit) = KJDABuilder(JDABuilder.createLight(token)).apply(builder).builder.build()
inline fun JDA(token: String, intents: List<GatewayIntent> = listOf(), builder: KJDABuilder.() -> Unit) = KJDABuilder(JDABuilder.create(token, intents)).apply(builder).build()

fun main() {
    DefaultJDA("") {
        intents {
            + (GatewayIntent.DIRECT_MESSAGES + GatewayIntent.DIRECT_MESSAGE_TYPING + GatewayIntent.GUILD_BANS)
        }
    }
}