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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.collection.isNotEmpty
import io.github.airdaydreamers.broadcastcommander.domain.repositories.ItemsRepositoryImpl
import io.github.airdaydreamers.broadcastcommander.domain.usecases.ExecuteCommandUseCase
import io.github.airdaydreamers.broadcastcommander.domain.usecases.GetItemsUseCase
import io.github.airdaydreamers.broadcastcommander.domain.usecases.GetSimpleItemsUseCase
import io.github.airdaydreamers.broadcastcommander.utils.doAsync
import io.github.airdaydreamers.broadcastcommander.utils.findCommandItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

//adb shell am broadcast -a io.github.airdaydreamers.commands.ACTION_COMMAND --es command "SIMPLE_ACTION_COMMAND_BUNDLE" io.github.airdaydreamers.demo
//adb shell am broadcast -a io.github.airdaydreamers.commands.ACTION_COMMAND --es command "ACTION_COMMAND_BUNDLE" io.github.airdaydreamers.demo
class CommandReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "CommandReceiver"
        const val ACTION_COMMAND = "io.github.airdaydreamers.commands.ACTION_COMMAND"
        const val EXTRA_COMMAND = "command"
        const val EXTRA_DATA = "data"
    }

    //TODO: Need to think about this
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private lateinit var executeUseCase: ExecuteCommandUseCase

    override fun onReceive(context: Context, intent: Intent?) = doAsync(scope) {
        Log.v(TAG, "onReceive: $intent for package: ${context.packageName}")

        val command = intent?.extras?.getString(EXTRA_COMMAND, null)
        if (intent?.action == ACTION_COMMAND && command != null) {
            Log.d(TAG, "command: $command")

            if (!this@CommandReceiver::executeUseCase.isInitialized) {
                Log.i(TAG, "Init ExecuteCommandUseCase")
                executeUseCase = ExecuteCommandUseCase(context)
            }

            //Load data use cases
            val simpleUseCase = GetSimpleItemsUseCase(ItemsRepositoryImpl(context))
            val commandsUseCase = GetItemsUseCase(ItemsRepositoryImpl(context))

            val commandsMap = simpleUseCase().takeIf { it.isSuccess }?.getOrNull()
            val commandsArray = commandsUseCase().takeIf { it.isSuccess }?.getOrNull()

            if (commandsMap?.isNotEmpty() == true) {
                commandsMap.let {
                    val commandItem = it[command]?.also {
                        executeUseCase(it.className, intent.extras)
                    }
                    if (commandItem == null) {
                        Log.w(TAG, "No found for command: $command")
                    }
                }
            } else if (commandsArray?.isNotEmpty() == true) {
                commandsArray.takeIf { it.isNotEmpty() }?.findCommandItem(command)?.let {
                    executeUseCase(it.className, intent.extras)
                }
            } else {
                //TODO: need to call from callbackManager
            }
        }
    }
}