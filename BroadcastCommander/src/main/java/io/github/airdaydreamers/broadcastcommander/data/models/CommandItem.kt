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
package io.github.airdaydreamers.broadcastcommander.data.models

import androidx.annotation.RestrictTo

/**
 * Represents a single command item with metadata.
 * @property rank The rank of the command.
 * @property command The command string.
 * @property className The fully qualified class name for the command handler.
 * @property type The type of the command.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class CommandItem(
    val rank: Int, val command: String, val className: String, val type: CommandType
) {

    private constructor(builder: Builder) : this(
        builder.rank, builder.command, builder.className, builder.type
    )

    companion object {
        /**
         * Builds a CommandItem instance using a DSL-style builder.
         * @param block The builder configuration block.
         * @return The constructed CommandItem.
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    /**
     * Builder class for CommandItem to ensure mandatory fields are set.
     */
    class Builder {
        var rank: Int = 0
        lateinit var command: String
        lateinit var className: String
        lateinit var type: CommandType

        /**
         * Builds and returns a CommandItem instance.
         * @throws IllegalArgumentException If mandatory fields are not set.
         */
        fun build(): CommandItem {
            if (!this::command.isInitialized || !this::className.isInitialized || !this::type.isInitialized) {
                throw IllegalArgumentException("Some parameter is null or empty")
            }

            return CommandItem(this)
        }
    }
}