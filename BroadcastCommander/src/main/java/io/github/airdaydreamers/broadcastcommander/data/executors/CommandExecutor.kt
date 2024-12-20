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
package io.github.airdaydreamers.broadcastcommander.data.executors

import android.os.Bundle
import androidx.annotation.RestrictTo

/**
 * A functional interface defining a Command Executor that executes commands
 * based on a class name and an optional bundle of arguments.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
fun interface CommandExecutor {
    /**
     * Executes a command using the provided class name.
     * @param className as [String] The fully qualified class name of the command to execute.
     */
    fun execute(className: String) {
        execute(className, null)
    }

    /**
     * Executes a command using the provided class name and arguments.
     * @param className as [String] The fully qualified class name of the command to execute.
     * @param bundle as [Bundle] Optional arguments for the command execution.
     */
    fun execute(className: String, bundle: Bundle?)
}