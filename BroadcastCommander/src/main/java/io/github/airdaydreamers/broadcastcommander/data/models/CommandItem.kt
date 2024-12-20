package io.github.airdaydreamers.broadcastcommander.data.models

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class CommandItem(
    val rank: Int, val command: String, val className: String, val type: CommandType
) {

    private constructor(builder: Builder) : this(
        builder.rank, builder.command, builder.className, builder.type
    )

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var rank: Int = 0
        lateinit var command: String
        lateinit var className: String
        lateinit var type: CommandType

        fun build(): CommandItem {
            if (!this::command.isInitialized || !this::className.isInitialized || !this::type.isInitialized) {
                throw IllegalArgumentException("Some parameter is null or empty")
            }

            return CommandItem(this)
        }
    }
}