package io.github.airdaydreamers.broadcastcommander.utils

import android.content.BroadcastReceiver
import androidx.collection.SparseArrayCompat
import io.github.airdaydreamers.broadcastcommander.data.models.CommandItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun BroadcastReceiver.doAsync(
    appScope: CoroutineScope,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult = goAsync()
    appScope.launch(coroutineContext) { block() }.invokeOnCompletion { pendingResult.finish() }
}

internal fun SparseArrayCompat<CommandItem>.legacyFindCommandItem(targetCommand: String): CommandItem? {
    for (i in 0 until size()) {
        val item = valueAt(i)
        if (item.command == targetCommand) {
            return item
        }
    }
    return null // Return null if no matching item is found
}

internal fun SparseArrayCompat<CommandItem>.findCommandItem(targetCommand: String): CommandItem? {
    return this.asSequence()
        .firstOrNull { it.command == targetCommand }
}

fun <T> SparseArrayCompat<T>.asSequence(): Sequence<T> {
    return sequence {
        for (i in 0 until size()) {
            yield(valueAt(i))
        }
    }
}