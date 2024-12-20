package io.github.airdaydreamers.broadcastcommander.domain.usecases

import androidx.annotation.RestrictTo
import androidx.collection.SparseArrayCompat
import io.github.airdaydreamers.broadcastcommander.data.models.CommandItem
import io.github.airdaydreamers.broadcastcommander.data.repositories.ItemsRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class GetItemsUseCase(private val itemsRepository: ItemsRepository) {
    suspend operator fun invoke(): Result<SparseArrayCompat<CommandItem>> =
        suspendCoroutine { continuation ->
            val result = itemsRepository.getCommandItemsFromXml()
            if (result.isEmpty()) {
                // or todo: continuation.resumeWithException(exception)
                return@suspendCoroutine continuation.resume(Result.failure(IllegalStateException("Empty list")))
            } else {
                return@suspendCoroutine continuation.resume(Result.success(result))
            }
        }
}