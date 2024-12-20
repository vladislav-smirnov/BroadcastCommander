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