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
package io.github.airdaydreamers.broadcastcommander.domain.services

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.RestrictTo
import io.github.airdaydreamers.broadcastcommander.data.executors.CommandExecutor

/**
 * A command executor that utilizes Java Reflection to dynamically invoke methods.
 * This executor finds and executes a handler class's methods.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class ReflectionCommandExecutor(private val context: Context) : CommandExecutor {
    companion object {
        private const val TAG = "ReflectionCommandExecutor"
    }

    /**
     * Executes a command using reflection.
     *
     * @param className The className of definition containing handler.
     * @param bundle as [Bundle] Additional data to be passed to the command's handler.
     */
    override fun execute(className: String, bundle: Bundle?) {
        Log.d(TAG, "execute")
        try {
            val moduleClass = Class.forName(className)
            val moduleInstance = moduleClass.getDeclaredConstructor().newInstance()

            // Look for a method named `invoke`
            val executeMethods = moduleClass.methods.filter { it.name == "invoke" }

            executeMethods.map { it.parameterTypes.size }
                .forEach { Log.v(TAG, "size: $it") }

            executeMethods.maxByOrNull { it.parameterTypes.size }?.let {
                Log.v(TAG, "execute each method")

                val parameterTypes = it.parameterTypes

                // Build arguments dynamically based on the method signature
                val args = mutableListOf<Any?>()
                for (parameterType in parameterTypes) {
                    Log.v(
                        TAG,
                        "parameterType: ${parameterType.name}"
                    )
                    when (parameterType) {
                        Context::class.java -> args.add(context)
                        Bundle::class.java -> args.add(bundle)
                        else -> args.add(null) // Handle unsupported parameters gracefully
                    }
                }

                // Invoke the method with the dynamically constructed arguments
                it.invoke(moduleInstance, *args.toTypedArray())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error invoking: ${e.message}", e)
        }
    }
}