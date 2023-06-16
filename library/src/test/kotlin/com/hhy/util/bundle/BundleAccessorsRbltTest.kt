package com.hhy.util.bundle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class BundleAccessorsRbltTest {
    companion object {
        const val KEY = "key"
        const val VALUE = "value"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ApplicationProvider.getApplicationContext<Context>().injectAsAppCtx()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun with_shouldSetSelfToCurrentBundle_whenInWithFunction() {
        val bundle = Bundle()
        val bundleSpec = BundleSpec()
        bundle.with(bundleSpec) {
            assertSame(bundle, currentBundle)
        }
        assertSame(null, bundleSpec.currentBundle)
    }

    @Test
    fun withExtras_shouldWorks_whenInIntent() {
        val bundleSpec = BundleSpec()
        val intent = Intent()
        intent.withExtras(bundleSpec) {
            assertTrue(isReadOnly)
            assertTrue(currentBundle!!.isEmpty)
        }
        assertFalse(bundleSpec.isReadOnly)
        assertNull(bundleSpec.currentBundle)

        intent.putExtra(KEY, VALUE)
        intent.withExtras(bundleSpec) {
            assertEquals(VALUE, currentBundle!!.get(KEY))
        }
    }

    @Test
    fun putExtras_shouldWorks_whenInIntent() {
        val bundleSpec = BundleSpec()
        val intent = Intent()
        assertNull(intent.extras)

        intent.putExtras(bundleSpec) { }
        val extras = intent.extras
        assertNotNull(extras)

        intent.putExtras(bundleSpec) {
            bundle.putString(KEY, VALUE)
        }
        assertEquals(VALUE, intent.getStringExtra(KEY))
    }
}
