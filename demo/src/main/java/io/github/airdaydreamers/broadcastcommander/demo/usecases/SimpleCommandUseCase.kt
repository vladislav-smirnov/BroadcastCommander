package io.github.airdaydreamers.broadcastcommander.demo.usecases

import android.os.Bundle
import android.util.Log
import io.github.airdaydreamers.broadcastcommander.demo.SimpleRepo

//adb shell am broadcast -a io.github.airdaydreamers.commands.ACTION_COMMAND --es command "SIMPLE_ACTION_COMMAND_BUNDLE" --es text "example one" io.github.airdaydreamers.broadcastcommander.demo
class SimpleCommandUseCase {
    operator fun invoke(bundle: Bundle){
        Log.i("CommandUseCase", "SimpleCommandUseCase: invoke")
        bundle.getString("text")?.let {
            SimpleRepo.updateText(it)
        }
    }
}