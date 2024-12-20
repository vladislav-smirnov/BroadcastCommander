package io.github.airdaydreamers.broadcastcommander.demo.usecases

import android.os.Bundle
import android.util.Log
import io.github.airdaydreamers.broadcastcommander.demo.SimpleRepo

class CommandUseCase {
    operator fun invoke(bundle: Bundle){
        Log.i("CommandUseCase", "CommandUseCase: invoke")
        bundle.getString("text")?.let {
            SimpleRepo.updateText(it)
        }
    }
}