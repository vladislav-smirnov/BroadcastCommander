package io.github.airdaydreamers.broadcastcommander.demo

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    //Notes: Should be as injection
    val repo = SimpleRepo

    val textData = repo.textFlow
}