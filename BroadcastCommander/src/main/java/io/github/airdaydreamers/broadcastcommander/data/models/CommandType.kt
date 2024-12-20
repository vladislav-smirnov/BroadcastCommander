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

import android.os.Bundle
import androidx.annotation.RestrictTo

/**
 * Enum representing the various types of commands supported by the lib.
 * @property value The integer value representing the type.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal enum class CommandType(val value: Int) {
    BOOLEAN(0),
    STRING(1),
    INT(2),
    BUNDLE(3),
    CALLBACK(4);

    companion object {
        /**
         * Determines the CommandType based on the provided class path.
         * @param classPath The fully qualified class path.
         * @return [CommandType] The corresponding CommandType.
         */
        fun fromClassPath(classPath: String): CommandType {
            val clazz = getClassFromFullPath(classPath)
            return when (clazz) {
                java.lang.String::class.java, String::class.java -> {
                    STRING
                }

                java.lang.Boolean::class.java, Boolean::class.java -> {
                    BOOLEAN
                }

                Bundle::class.java -> {
                    BUNDLE
                }

                else -> BUNDLE
            }
        }

        /**
         * Converts a string representation of an integer value to a CommandType.
         * @param value as [String] The string representation of the integer value.
         * @return [CommandType] The corresponding CommandType.
         * @throws IllegalArgumentException If the value does not match any CommandType.
         */
        @Throws(
            IllegalArgumentException::class,
            NumberFormatException::class
        )
        fun fromValue(value: String): CommandType {
            return fromValue(value.toInt())
        }

        /**
         * Converts an integer value to a CommandType.
         * @param value The integer value.
         * @return [CommandType] The corresponding CommandType.
         * @throws IllegalArgumentException If the value does not match any CommandType.
         */
        @Throws(IllegalArgumentException::class)
        fun fromValue(value: Int): CommandType {
            val enum = entries.find { it.value == value }
            if (enum == null) {
                throw IllegalArgumentException("Incorrect value type")
            }
            return enum
        }

        private fun getClassFromFullPath(classPath: String): Class<*>? {
            return try {
                Class.forName(classPath)
            } catch (e: ClassNotFoundException) {
                //Class was not found
                null
            }
        }
    }
}