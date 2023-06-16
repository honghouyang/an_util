package com.hhy.util.io

import java.io.Closeable
import java.io.IOException

/** Do safe action on a [Closeable]. */
fun <T : Closeable> T.doSafe(action: T.() -> Unit) {
    try {
        action()
    } catch (ignored: IOException) {
        // ignored
    } finally {
        closeQuietly()
    }
}

/** Close a [Closeable] unconditionally. */
fun <T : Closeable> T?.closeQuietly() {
    try {
        this?.close()
    } catch (ignored: Exception) {
        // ignored
    }
}

/** Close all [Closeable]s of array after [block] is operated. */
fun <T : Closeable?> Array<T>.use(block: () -> Unit) {
    var exception: Throwable? = null
    try {
        return block()
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        when (exception) {
            null -> forEach { it?.close() }
            else -> forEach {
                try {
                    it?.close()
                } catch (ignored: Throwable) {
                    // ignored
                }
            }
        }
    }
}
