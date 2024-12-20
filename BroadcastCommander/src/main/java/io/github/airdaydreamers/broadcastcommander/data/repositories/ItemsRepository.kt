package io.github.airdaydreamers.broadcastcommander.data.repositories

import androidx.annotation.RestrictTo
import androidx.collection.SparseArrayCompat
import io.github.airdaydreamers.broadcastcommander.data.models.CommandItem

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal interface ItemsRepository {
    fun getItemsFromXml(): Map<String, CommandItem>
    fun getCommandItemsFromXml(): SparseArrayCompat<CommandItem>
}