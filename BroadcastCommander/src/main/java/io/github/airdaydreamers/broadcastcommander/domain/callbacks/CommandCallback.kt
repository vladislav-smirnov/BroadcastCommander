package io.github.airdaydreamers.broadcastcommander.domain.callbacks

import android.os.Bundle

fun interface CommandCallback {
    fun onCommandReceived(data: Bundle?)
}