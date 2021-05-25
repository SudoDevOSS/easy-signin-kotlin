package com.kabdev.modules.networking.extensions

fun <T> T?.unwrapOrThrowIfNull(errorMessage: String? = null): T {
    if (this == null) {
        throw NullPointerException(errorMessage)
    }
    return this
}