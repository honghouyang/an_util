package com.hhy.util.bundle

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import com.hhy.util.thread.isMainThread

open class BundleSpec {
    @PublishedApi
    internal var currentBundle: Bundle?
        get() = if (isMainThread) currentBundleMainThread else currentBundleByThread.get()
        set(value) {
            if (isMainThread) currentBundleMainThread = value else currentBundleByThread.set(value)
        }

    @PublishedApi
    internal var isReadOnly: Boolean
        get() = if (isMainThread) isReadOnlyMainThread else isReadOnlyByThread.get() ?: false
        set(value) {
            if (isMainThread) isReadOnlyMainThread = value else isReadOnlyByThread.set(value)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var currentBundleMainThread: Bundle? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val currentBundleByThread by lazy { ThreadLocal<Bundle?>() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var isReadOnlyMainThread = false

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val isReadOnlyByThread by lazy { ThreadLocal<Boolean>() }
}
