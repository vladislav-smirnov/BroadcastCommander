package io.github.airdaydreamers.broadcastcommander.demo

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SimpleRepo {
    private var _textFlow = MutableStateFlow<String>("INIT")
    val textFlow: StateFlow<String> = _textFlow

    fun updateText(value: String) {
        _textFlow.tryEmit(value)
    }
}