/*
 * Copyright © 2024 Vladislav Smirnov
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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