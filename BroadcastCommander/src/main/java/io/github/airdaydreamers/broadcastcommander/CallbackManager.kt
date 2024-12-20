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
package io.github.airdaydreamers.broadcastcommander

import android.os.Bundle
import android.util.Log
import io.github.airdaydreamers.broadcastcommander.domain.callbacks.CommandCallback
import java.util.concurrent.CopyOnWriteArrayList

object CallbackManager {
    private const val TAG = "CallbackManager"

    //TODO: add weak reference
    private val callbackList = CopyOnWriteArrayList<CommandCallback>()

    fun registerCallback(callback: CommandCallback) {
        if (!callbackList.contains(callback)) {
            callbackList.add(callback)
        }
    }

    fun unregisterCallback(callback: CommandCallback) {
        callbackList.remove(callback)
    }

    // Notify all callbacks (invoke for each)
    fun notifyCallbacks(data: Bundle?) {
        for (callback in callbackList) {
            try {
                callback.onCommandReceived(data)
            } catch (e: Exception) {
                // Log or handle exceptions in individual callbacks
                Log.e(TAG, "Error in callback: ${e.message}", e)
            }

            //TODO: weak reference
            // Clean up any null references
            //callbackList.removeAll { it.get() == null }
        }
    }
}