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
package io.github.airdaydreamers.broadcastcommander.domain.repositories

import android.content.Context
import android.content.res.TypedArray
import android.content.res.XmlResourceParser
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import androidx.annotation.RestrictTo
import androidx.collection.SparseArrayCompat
import androidx.collection.isNotEmpty
import io.github.airdaydreamers.broadcastcommander.R
import io.github.airdaydreamers.broadcastcommander.data.models.CommandItem
import io.github.airdaydreamers.broadcastcommander.data.models.CommandType
import io.github.airdaydreamers.broadcastcommander.data.repositories.ItemsRepository
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import kotlin.use

/**
 * Implementation of the ItemsRepository interface.
 * This class handles parsing XML files and loading command items into memory.
 *
 * @property context as [Context] The application context used for accessing resources.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class ItemsRepositoryImpl(private val context: Context) : ItemsRepository {
    companion object {
        private const val TAG = "ItemsRepositoryImpl"
        private const val XML_TAG_COMMAND_ITEMS = "commandItems";
        private const val XML_TAG_COMMAND_ITEM = "item";
    }

    private val commandMap: MutableMap<String, CommandItem> = mutableMapOf()

    //TODO: do we really need SparseArray. It looks too much
    private val commandItems: SparseArrayCompat<CommandItem> = SparseArrayCompat<CommandItem>()

    override fun getItemsFromXml(): Map<String, CommandItem> {
        Log.v(TAG, "getItemsFromXml")
        if (commandMap.isNotEmpty()) {
            Log.v(TAG, "[getItemsFromXml] return list from memory")
            return commandMap
        }

        context.resources.getXml(R.xml.command_mappings_simple).use { parser ->
            var eventType = parser.eventType

            var rank = 0

            while (eventType != XmlPullParser.END_DOCUMENT) {
                Log.d(TAG, "parser.name: ${parser.name}")
                if (eventType == XmlPullParser.START_TAG && parser.name == "command") {
                    val action = parser.getAttributeValue(null, "action")
                    val handler = parser.getAttributeValue(null, "handler")
                    val type = parser.getAttributeValue(null, "type") ?: "android.os.Bundle"

                    Log.i(
                        TAG,
                        "rank: $rank command: $action className: $handler type: ${
                            CommandType.Companion.fromClassPath(type)
                        }"
                    )

                    if (!action.isNullOrBlank() && !handler.isNullOrBlank()) {
                        val item = CommandItem.Companion.build {
                            this.rank = rank
                            command = action
                            className = handler
                            this.type = CommandType.Companion.fromClassPath(type)
                        }
                        rank++

                        commandMap[action] = item
                    }
                }
                eventType = parser.next()
            }
        }
        return commandMap
    }


    @Throws(RuntimeException::class)
    override fun getCommandItemsFromXml(): SparseArrayCompat<CommandItem> {
//        if (DEBUG) {
//            Log.i(TAG, "getCommandItemsFromXml start");
//        }
        if (commandItems.isNotEmpty()) {
            return commandItems
        }

        try {
            context.resources.getXml(R.xml.command_mappings).use { parser ->
                val attrs: AttributeSet = Xml.asAttributeSet(parser)
                var type: Int

                while (parser.next().also { type = it } != XmlResourceParser.END_DOCUMENT
                    && type != XmlResourceParser.START_TAG) {
                    // Do Nothing (moving parser to start element)
                }

                if (parser.name != XML_TAG_COMMAND_ITEMS) {
                    throw RuntimeException("Meta-data does not start with commandItems tag")
                }

                val outerDepth = parser.depth
                var rank = 0

                while (parser.next().also { type = it } != XmlResourceParser.END_DOCUMENT
                    && (type != XmlResourceParser.END_TAG || parser.depth > outerDepth)) {
                    if (type == XmlResourceParser.END_TAG) {
                        continue
                    }

                    if (parser.name == XML_TAG_COMMAND_ITEM) {
                        val item = context.resources.obtainAttributes(
                            attrs, R.styleable.commandItems_item
                        )

                        val needIncrement: Boolean
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            item.use { typedArray ->
                                if (addItem(typedArray, rank)) rank++
                            }
                        } else {
                            needIncrement = addItem(item, rank)

                            item.recycle()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is XmlPullParserException, is IOException -> {
                    Log.e(TAG, "Error parsing items", e)
                }

                else -> throw e
            }
        }

//        if (DEBUG) {
//            Log.i(TAG, "getCommandItemsFromXml finished. Number of items: ${commandItems.size}")
//        }

        return commandItems
    }


    private fun addItem(item: TypedArray, rank: Int): Boolean {
        val type = item.getInt(
            R.styleable.commandItems_item_type,
            4 // defValue
        )

        //Just for visualise
        val xmlValue = object {
            val command = item.getString(
                R.styleable.commandItems_item_command,
            )
            val className = item.getString(
                R.styleable.commandItems_item_className,
            )
        }

        if (xmlValue.command.isNullOrBlank() || xmlValue.className.isNullOrBlank()) {
            //skip incorrect item
            return false
        }

        Log.i(
            TAG,
            "rank: $rank xmlValue.command: ${xmlValue.command} xmlValue.className: ${xmlValue.className} type: ${
                CommandType.Companion.fromValue(type)
            }"
        )

        val commandItem = CommandItem.Companion.build {
            this.rank = rank
            command = xmlValue.command
            className = xmlValue.className
            this.type = CommandType.Companion.fromValue(type)
        }

        commandItems.put(type, commandItem)
        return true
    }
}