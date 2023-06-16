package com.hhy.util.bundle

import android.content.Intent
import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

@RunWith(AndroidJUnit4::class)
@SmallTest
class BundleDelegatesTest {
    companion object {
        const val KEY = "key"
        const val VALUE = "value"
        const val DEFAULT_KEY = "default key"
        const val DEFAULT_VALUE = "default value"
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
    fun bundle_shouldReturnCurrentBundle_whenInWithFunction() {
        val bundleX = Bundle()
        val bundleSpec = BundleSpec()
        bundleX.with(bundleSpec) {
            assertSame(bundle, currentBundle)
        }
    }

    @Test
    fun bundle_shouldThrowException_whenOutOfWithFunction() {
        val bundleSpec = BundleSpec()
        assertFailsWith<Exception> {
            bundleSpec.bundle
        }
    }

    @Test
    fun put_shouldSaveDataToBundle_whenInBundleSpec() {
        val bundleSpec = BundleSpec()
        val bundleX = Bundle()
        bundleX.with(bundleSpec) {
            put(KEY, VALUE)
            assertEquals(VALUE, bundle.get(KEY))
        }
    }

    @Test
    fun put_shouldThrowException_whenInBundleSpec() {
        val bundleSpec = BundleSpec()
        val intent = Intent()
        intent.withExtras(bundleSpec) {
            assertFailsWith<Exception> { put(KEY, VALUE) }
        }
    }

    @Test
    fun bundle_shouldSupportDelegate() {
        val intent = Intent()
        val bundleSpec = object : BundleSpec() {
            var key: String by bundle()
            var value: String by bundle()
        }

        intent.putExtras(bundleSpec) {
            assertFailsWith<Exception> {
                key
            }

            key = KEY
            value = VALUE
        }

        intent.withExtras(bundleSpec) {
            assertEquals(KEY, key)
            assertEquals(VALUE, value)
        }
    }

    @Test
    fun bundleOrDefault_shouldSupportDelegate() {
        val intent = Intent()
        val bundleSpec = object : BundleSpec() {
            var key: String by bundleOrDefault(DEFAULT_KEY)
            var value: String by bundleOrDefault(DEFAULT_VALUE)
        }

        intent.putExtras(bundleSpec) {
            assertEquals(DEFAULT_KEY, key)
            assertEquals(DEFAULT_VALUE, value)

            key = KEY
            value = VALUE
        }

        intent.withExtras(bundleSpec) {
            assertEquals(KEY, key)
            assertEquals(VALUE, value)
        }
    }

    @Test
    fun bundleOrNull_shouldSupportDelegate() {
        val intent = Intent()
        val bundleSpec = object : BundleSpec() {
            var key: String? by bundleOrNull()
            var value: String? by bundleOrNull()
        }

        intent.putExtras(bundleSpec) {
            assertNull(key)
            assertNull(value)

            key = KEY
            value = VALUE
        }

        intent.withExtras(bundleSpec) {
            assertEquals(KEY, key)
            assertEquals(VALUE, value)
        }
    }

    @Test
    fun bundleOrElse_shouldSupportDelegate() {
        val intent = Intent()
        val bundleSpec = object : BundleSpec() {
            var key: String by bundleOrElse {
                DEFAULT_KEY
            }
            var value: String by bundleOrElse {
                DEFAULT_VALUE
            }
        }

        intent.putExtras(bundleSpec) {
            assertEquals(DEFAULT_KEY, key)
            assertEquals(DEFAULT_VALUE, value)

            key = KEY
            value = VALUE
        }

        intent.withExtras(bundleSpec) {
            assertEquals(KEY, key)
            assertEquals(VALUE, value)
        }
    }

    @Test
    fun bundle_shouldSupportDelegate_whenWithKey() {
        val intent = Intent()
        val bundleSpec = object : BundleSpec() {
            var key: String by bundle(KEY)
            var value: String by bundle(KEY)
        }

        intent.putExtras(bundleSpec) {
            assertFailsWith<Exception> {
                key
            }

            key = KEY
        }

        intent.withExtras(bundleSpec) {
            assertEquals(KEY, key)
            assertEquals(KEY, value)
        }
    }

    @Test
    fun bundleOrNull_shouldSupportDelegate_whenWithKey() {
        val intent = Intent()
        val bundleSpec = object : BundleSpec() {
            var key: String? by bundleOrNull(KEY)
            var value: String? by bundleOrNull(KEY)
        }

        intent.putExtras(bundleSpec) {
            assertNull(key)
            assertNull(value)

            key = KEY
        }

        intent.withExtras(bundleSpec) {
            assertEquals(KEY, key)
            assertEquals(KEY, value)
        }
    }
}
