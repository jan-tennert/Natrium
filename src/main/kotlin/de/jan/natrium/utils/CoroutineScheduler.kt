package de.jan.natrium.utils

import kotlinx.coroutines.delay
import java.time.Duration

suspend fun schedule(delay: Duration, task: suspend () -> Unit) {
    delay(delay.toMillis())
    task()
}

suspend fun scheduleRepeating(delay: Duration, delayBetweenTasks: Duration = Duration.ZERO, task: suspend () -> Unit) {
    task()
    while(true) {
        delay(delay.toMillis())
        task()
        if(!delayBetweenTasks.isZero) delay(delayBetweenTasks.toMillis())
    }
}