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
package io.github.airdaydreamers.broadcastcommander.domain.callbacks

import android.os.Bundle

/**
 * Interface for handling callbacks when a command is executed.
 */
fun interface CommandCallback {
    /**
     * Called when a command was received successfully.
     *
     * @param data as [Bundle] The arguments of the command.
     */
    fun onCommandReceived(data: Bundle?)
}