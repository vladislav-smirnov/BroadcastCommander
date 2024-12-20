package io.github.airdaydreamers.broadcastcommander.data.executors

import android.os.Bundle
import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun interface CommandExecutor {
    fun execute(className: String) {
        execute(className, null)
    }

    fun execute(className: String, bundle: Bundle?)
}