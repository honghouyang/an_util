package com.hhy.util.os

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowBuild
import org.robolectric.shadows.ShadowEnvironment
import org.robolectric.util.ReflectionHelpers
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class SystemTest {
    companion object {
        const val MANUFACTURER = "manufacturer"
        const val MODEL = "model"
        const val PRODUCT = "product"
        const val BRAND = "brand"
        const val VERSION_SDK_INT = Build.VERSION_CODES.KITKAT
        const val VERSION_RELEASE = "version_release"
        const val DISPLAY = "display"
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
    fun manufacturer_shouldReturnsOEMManufacturer() {
        ShadowBuild.setManufacturer(MANUFACTURER)
        assertEquals(manufacturer, MANUFACTURER)
    }

    @Test
    fun model_shouldReturnsOEMModel() {
        ShadowBuild.setModel(MODEL)
        assertEquals(model, MODEL)
    }

    @Test
    fun product_shouldReturnsOEMProduct() {
        ShadowBuild.setProduct(PRODUCT)
        assertEquals(product, PRODUCT)
    }

    @Test
    fun brand_shouldReturnsOEMBrand() {
        ShadowBuild.setBrand(BRAND)
        assertEquals(brand, BRAND)
    }

    @Test
    fun osVerCode_shouldReturnsOEMVersionSdkInt() {
        ReflectionHelpers.setStaticField(VERSION::class.java, "SDK_INT", VERSION_SDK_INT)
        assertEquals(osVerCode, VERSION_SDK_INT)
    }

    @Test
    fun osVerName_shouldReturnsOEMVersionRelease() {
        ShadowBuild.setVersionRelease(VERSION_RELEASE)
        assertEquals(osVerName, VERSION_RELEASE)
    }

    @Test
    fun osVerDisplayName_shouldReturnsOEMDisplay() {
        ReflectionHelpers.setStaticField(Build::class.java, "DISPLAY", DISPLAY)
        assertEquals(osVerDisplayName, DISPLAY)
    }

    @Suppress("DEPRECATION")
    @Test
    fun appVerCode_shouldReturnAppVersionCode_whenNoPkgName() {
        val ver = 10
        val packageInfo = PackageInfo()
        packageInfo.versionCode = ver
        val context = mockk<Context> {
            every {
                packageManager.getPackageInfo(any<String>(), any())
            } returns packageInfo
        }
        context.injectAsAppCtx()

        assertEquals(ver, appVerCode())
        every { context.packageManager.getPackageInfo(any<String>(), any()) }
    }

    @Suppress("DEPRECATION")
    @Test
    fun appVerCode_shouldReturnAppVersionCode_whenWithPkgName() {
        val ver = 10
        val packageInfo = PackageInfo()
        packageInfo.versionCode = ver
        val context = mockk<Context> {
            every {
                packageManager.getPackageInfo(any<String>(), any())
            } returns packageInfo
        }
        context.injectAsAppCtx()

        assertEquals(ver, appVerCode("com.hhy.util"))
        every { context.packageManager.getPackageInfo(any<String>(), any()) }
    }

    @Test
    fun appVerName_shouldReturnAppVersionCode_whenNoPkgName() {
        val ver = "1.0.0"
        val packageInfo = PackageInfo()
        packageInfo.versionName = ver
        val context = mockk<Context> {
            every {
                packageManager.getPackageInfo(any<String>(), any())
            } returns packageInfo
        }
        context.injectAsAppCtx()

        assertEquals(ver, appVerName())
        every { context.packageManager.getPackageInfo(any<String>(), any()) }
    }

    @Test
    fun appVerName_shouldReturnAppVersionName_whenWithPkgName() {
        val ver = "1.0.0"
        val packageInfo = PackageInfo()
        packageInfo.versionName = ver
        val context = mockk<Context> {
            every {
                packageManager.getPackageInfo(any<String>(), any())
            } returns packageInfo
        }
        context.injectAsAppCtx()

        assertEquals(ver, appVerName("com.hhy.util"))
        every { context.packageManager.getPackageInfo(any<String>(), any()) }
    }

    @Test
    fun appVerName_shouldReturnEmpty_whenAppVersionNameIsNull() {
        val packageInfo = PackageInfo()
        packageInfo.versionName = null
        val context = mockk<Context> {
            every {
                packageManager.getPackageInfo(any<String>(), any())
            } returns packageInfo
        }
        context.injectAsAppCtx()

        assertEquals("", appVerName())
        every { context.packageManager.getPackageInfo(any<String>(), any()) }
    }

    @Test
    fun language_shouldChange_whenLocaleChange() {
        Locale.setDefault(Locale("zh", "CN"))
        assertEquals("zh", language)

        Locale.setDefault(Locale("en", "US"))
        assertEquals("en", language)
    }

    @Test
    fun languageAndCountry_shouldChange_whenLocaleAndCountryChange() {
        Locale.setDefault(Locale("zh", "CN"))
        assertEquals("zh-CN", languageAndCountry)

        Locale.setDefault(Locale("zh", "TW"))
        assertEquals("zh-TW", languageAndCountry)

        Locale.setDefault(Locale("en", "US"))
        assertEquals("en-US", languageAndCountry)
    }

    @Test
    fun externalStorageWritable_shouldRelateToMountedState() {
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED)
        assertTrue { externalStorageWritable }

        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED)
        assertFalse { externalStorageWritable }
    }

    @Test
    fun externalStorageReadable_shouldRelateToMountedState() {
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED)
        assertTrue { externalStorageReadable }

        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED_READ_ONLY)
        assertTrue { externalStorageReadable }

        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED)
        assertFalse { externalStorageWritable }
    }

    @Test
    fun root_shouldReturnTrue_whenEchoRootSucceed() {
        mockkStatic("com.hhy.util.os.ShellKt")
        every { execCmd(any(), root = true, outputMsg = false) } returns CmdExecResult(
            0,
            null,
            null
        )
        assertTrue { root }
    }

    @Test
    fun isRoot_shouldReturnFalse_whenEchoRootFail() {
        mockkStatic("com.hhy.util.os.ShellKt")
        every { execCmd(any(), root = true, outputMsg = false) } returns CmdExecResult(
            -1,
            null,
            null
        )
        assertFalse { root }
    }
}
