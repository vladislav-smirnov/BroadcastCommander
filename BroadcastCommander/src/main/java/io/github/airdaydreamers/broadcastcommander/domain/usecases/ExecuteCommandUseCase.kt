package io.github.airdaydreamers.broadcastcommander.domain.usecases

import android.content.Context
import android.os.Bundle
import androidx.annotation.RestrictTo
import io.github.airdaydreamers.broadcastcommander.data.executors.CommandExecutor
import io.github.airdaydreamers.broadcastcommander.domain.services.ReflectionCommandExecutor

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class ExecuteCommandUseCase(
    context: Context,
    private val executor: CommandExecutor = ReflectionCommandExecutor(
        context
    )
) {

    operator fun invoke(className: String, bundle: Bundle? = null) {
        executor.execute(className, bundle)
    }
}