package com.hhy.util.os

import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SmallTest
class SystemAndroidTest {
    companion object {
        const val PROCESS_NAME = "com.hhy.util.test"
    }

    @Test
    fun guid_shouldBeSame() {
        repeat(10) {
            assertEquals(guid, guid)
        }
    }

    @Test
    fun installationContent_shouldBeSame() {
        File(appCtx.filesDir, "INSTALLATION").deleteOnExit()
        repeat(10) {
            assertEquals(installationContent(), installationContent())
        }
    }

    @Test
    fun processName_shouldAlwaysBeAppId() {
        repeat(10) {
            assertEquals(PROCESS_NAME, processName)
        }
    }

    @Test
    fun memTotalSize_shouldMoreThanZero() {
        repeat(10) {
            assertTrue { memTotalSize > 0 }
        }
    }

    @Test
    fun memAvailableSize_shouldMoreThanZero() {
        repeat(10) {
            assertTrue { memAvailableSize > 0 }
        }
    }

    @Test
    fun memAvailablePercent_shouldMoreThanZeroAndLessThanOneHundred() {
        repeat(10) {
            val percentText = memAvailablePercent
            assertNotNull(percentText)

            val percent = percentText.toDouble()
            assertTrue {
                percent > 0 && percent < 100
            }
        }
    }
}
