package de.jan.natrium.utils

import kotlinx.coroutines.delay
import java.time.Duration

suspend inline fun schedule(delay: Duration, task: () -> Unit) {
    delay(delay.toMillis())
    task()
}

suspend inline fun scheduleRepeating(delay: Duration, delayBetweenTasks: Duration = Duration.ZERO, task: () -> Unit) {
    task()
    while(true) {
        delay(delay.toMillis())
        task()
        if(!delayBetweenTasks.isZero) delay(delayBetweenTasks.toMillis())
    }
}