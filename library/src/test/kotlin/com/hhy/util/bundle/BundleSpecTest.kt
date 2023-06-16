package com.hhy.util.bundle

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.thread
import kotlin.test.assertSame

@RunWith(AndroidJUnit4::class)
@SmallTest
class BundleSpecTest {
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun currentBundle_shouldUseCurrentBundleMainThreadAsField_whenInMainThread() {
        val bundleSpec = BundleSpec()
        assertSame(bundleSpec.currentBundleMainThread, bundleSpec.currentBundle)
        val bundle = Bundle()
        bundleSpec.currentBundle = bundle
        assertSame(bundle, bundleSpec.currentBundleMainThread)
    }

    @Test
    fun currentBundle_shouldUseCurrentBundleByThreadAsField_whenInOtherThread() {
        thread {
            val bundleSpec = BundleSpec()
            assertSame(bundleSpec.currentBundleByThread.get(), bundleSpec.currentBundle)
            val bundle = Bundle()
            bundleSpec.currentBundle = bundle
            assertSame(bundle, bundleSpec.currentBundleByThread.get())
        }
    }

    @Test
    fun isReadOnly_shouldUseIsReadOnlyMainThreadAsField_whenInMainThread() {
        val bundleSpec = BundleSpec()
        assertSame(bundleSpec.isReadOnlyMainThread, bundleSpec.isReadOnly)
        bundleSpec.isReadOnly = true
        assertSame(true, bundleSpec.isReadOnly)
    }

    @Test
    fun isReadOnly_shouldUseIsReadOnlyByThreadAsField_whenInOtherThread() {
        thread {
            val bundleSpec = BundleSpec()
            assertSame(bundleSpec.isReadOnlyByThread.get() ?: false, bundleSpec.isReadOnly)
            bundleSpec.isReadOnly = true
            assertSame(true, bundleSpec.isReadOnlyByThread.get())
            assertSame(bundleSpec.isReadOnlyByThread.get(), bundleSpec.isReadOnly)
        }
    }
}
