package com.hhy.util.bundle

import android.app.Activity
import android.content.Intent
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@SmallTest
class BundleAccessorsTest {
    companion object {
        const val KEY = "key"
        const val VALUE = "value"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun withExtras_shouldWorks_whenInActivity() {
        mockkStatic("com.hhy.util.bundle.BundleAccessorsKt")
        val intentX = mockk<Intent>()
        val activity = mockk<Activity> {
            every { intent } returns intentX
        }
        val bundleSpec = BundleSpec()
        every { intentX.withExtras(any(), any<BundleSpec.() -> String>()) } returns VALUE
        val value = activity.withExtras(bundleSpec) { KEY }
        assertEquals(VALUE, value)
        verify {
            activity.intent
            intentX.withExtras(any(), any())
        }
    }

    @Test
    fun putExtras_shouldWorks_whenInActivity() {
        mockkStatic("com.hhy.util.bundle.BundleAccessorsKt")
        val intentX = mockk<Intent>()
        val activity = mockk<Activity> {
            every { intent } returns intentX
        }
        val bundleSpec = BundleSpec()
        every { intentX.putExtras(any(), any()) } returns Unit
        activity.putExtras(bundleSpec) {}
        verify {
            activity.intent
            intentX.putExtras(any(), any())
        }
    }
}
