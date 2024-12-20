package io.github.airdaydreamers.broadcastcommander.domain.services

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.RestrictTo
import io.github.airdaydreamers.broadcastcommander.data.executors.CommandExecutor

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class ReflectionCommandExecutor(private val context: Context) : CommandExecutor {
    companion object {
        private const val TAG = "ReflectionCommandExecutor"
    }

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