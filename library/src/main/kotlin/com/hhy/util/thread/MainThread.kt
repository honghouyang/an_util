package com.hhy.util.thread

import android.os.Looper

/** This main looper cache avoids synchronization overhead when accessed repeatedly. */
@JvmField
val mainLooper: Looper = Looper.getMainLooper()

@JvmField
val mainThread: Thread = mainLooper.thread

val currentThread get() = Thread.currentThread()

val isMainThread get() = mainThread === currentThread

val currentThreadName: String get() = currentThread.name

/**
 * Passes if run on the [mainThread] (aka. UI thread), throws an [IllegalStateException] otherwise.
 */
fun checkMainThread() = check(isMainThread) {
    "This should ONLY be called on the main thread! Current: ${Thread.currentThread()}"
}

/**
 * Passes if not run on the [mainThread] (aka. UI thread), throws an [IllegalStateException]
 * otherwise.
 */
fun checkNotMainThread() = check(!isMainThread) {
    "This should NEVER be called on the main thread! Current: ${Thread.currentThread()}"
}
