package com.sudodevoss.easysignin.networking.core

import com.squareup.moshi.Moshi

/**
 * Base JSON Parser class
 *
 * Exposes functions to serialize and deserialize objects to JSON
 * Relies on Moshi library
 * @see [Moshi]
 */
object BaseJsonParser {
    /**
     * Moshi shared instance
     */
    val moshi: Moshi = Moshi.Builder().build()

    /**
     * Deserialize JSON string to object [T]
     *
     * @return [T] instance from parsed json or null if failed to parse
     */
    inline fun <reified T> parseJsonString(input: String): T? {
        val adapter = moshi.adapter(T::class.java)
        return adapter.fromJson(input)
    }

    /**
     * Serialize [T] to JSON string
     *
     * @param input [T] instance to serialize
     * @return [String] JSON representation of [T]
     */
    inline fun <reified T> toJson(input: T): String {
        return moshi.adapter(T::class.java).toJson(input)
    }
}