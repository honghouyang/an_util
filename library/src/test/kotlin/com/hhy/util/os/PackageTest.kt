package com.hhy.util.os

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.initializer.ThreeTenAbpInitializer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SmallTest
class PackageTest {
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun installedApps_shouldReturnAllApps() {
        val piA = PackageInfo()
        piA.packageName = "com.a"
        piA.versionName = "1.0.0"

        val piB = PackageInfo()
        piB.packageName = "com.b"
        piB.versionName = "2.0.0"

        val piNull = PackageInfo()
        piNull.packageName = "com.null"
        piNull.versionName = null

        val infos = mutableListOf(piA, piB, piNull)

        val context = mockk<Context> {
            val a: CharSequence = "a"
            val b: CharSequence = "b"
            every { packageManager.getApplicationLabel(any()) } returnsMany listOf(a, b, "null")
            every {
                packageManager.getInstalledPackages(0)
            } returns infos
        }
        context.injectAsAppCtx()

        val apps = installedApps
        apps.forEach {
            when (it.name) {
                "a" -> {
                    assertEquals(piA.packageName, it.pkgName)
                    assertEquals(piA.versionName, it.versionName)
                }
                "b" -> {
                    assertEquals(piB.packageName, it.pkgName)
                    assertEquals(piB.versionName, it.versionName)
                }
                "" -> {
                    assertEquals(piNull.packageName, it.pkgName)
                    assertEquals("", it.versionName)
                }
            }
        }
    }

    @Test
    fun isInstalled_shouldReturnTrue_whenExist() {
        val context = mockk<Context> {
            every { packageManager } returns mockk {
                every { getPackageInfo("a", any()) } returns spyk()
            }
        }
        context.injectAsAppCtx()

        println(appCtx.packageManager.getPackageInfo("a", 0))
        assertTrue { isInstalled("a") }
    }

    @Test
    fun isInstalled_shouldReturnFalse_whenNotExist() {
        val context = mockk<Context> {
            every { packageManager } returns mockk {
                every { getPackageInfo("a", any()) } throws PackageManager.NameNotFoundException()
            }
        }
        context.injectAsAppCtx()

        assertFalse { isInstalled("a") }
    }

    @Test
    fun isMainProcess_shouldWorks() {
        val context = mockk<Context> {
            every {
                packageName
            } returns "a" andThen "b"
        }
        context.injectAsAppCtx()

        mockkStatic("com.hhy.util.os.SystemKt")
        every { processName } returns "a"

        assertTrue(isMainProcess())
        assertFalse(isMainProcess())
    }

    @Test
    fun isComponentEnabled_shouldWorks() {
        val context = mockk<Context> {
            every {
                packageManager.getComponentEnabledSetting(any())
            } returns PackageManager.COMPONENT_ENABLED_STATE_ENABLED andThen
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
        context.injectAsAppCtx()

        assertTrue(isComponentEnabled(ThreeTenAbpInitializer::class.java))
        assertFalse(isComponentEnabled(ThreeTenAbpInitializer::class.java))
    }

    @Test
    fun isComponentDisabled_shouldWorks() {
        val context = mockk<Context> {
            every {
                packageManager.getComponentEnabledSetting(any())
            } returns PackageManager.COMPONENT_ENABLED_STATE_ENABLED andThen
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
        context.injectAsAppCtx()

        assertFalse(isComponentDisabled(ThreeTenAbpInitializer::class.java))
        assertTrue(isComponentDisabled(ThreeTenAbpInitializer::class.java))
    }

    @Test
    fun setComponentState_shouldWorks() {
        val context = mockk<Context> {
            every {
                packageManager.getComponentEnabledSetting(any())
            } returns PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
        context.injectAsAppCtx()

        setComponentState(ThreeTenAbpInitializer::class.java, true)
        setComponentState(ThreeTenAbpInitializer::class.java, false)
    }

    @Test
    fun enableComponent_shouldWorks() {
        val context = mockk<Context>()
        context.injectAsAppCtx()
        enableComponent(ThreeTenAbpInitializer::class.java)
    }

    @Test
    fun disableComponent_shouldWorks() {
        val context = mockk<Context>()
        context.injectAsAppCtx()
        disableComponent(ThreeTenAbpInitializer::class.java)
    }
}
