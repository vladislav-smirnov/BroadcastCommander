package io.github.airdaydreamers.broadcastcommander.data.models

import android.os.Bundle
import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal enum class CommandType(val value: Int) {
    BOOLEAN(0),
    STRING(1),
    INT(2),
    BUNDLE(3),
    CALLBACK(4);

    companion object {
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

        @Throws(
            IllegalArgumentException::class,
            NumberFormatException::class
        )
        fun fromValue(value: String): CommandType {
            return fromValue(value.toInt())
        }

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
                println("Class not found: $classPath")
                null
            }
        }
    }
}