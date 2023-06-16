package com.hhy.util.dimension

import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.view.Display
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.os.windowManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowDisplay
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@SmallTest
class DimensionRbltTest {
    private lateinit var display: Display
    private lateinit var shadowDisplay: ShadowDisplay

    companion object {
        const val HEIGHT = 600
        const val WIDTH = 400
        const val REAL_HEIGHT = 800
        const val REAL_WIDTH = 480
        const val STATUS_BAR_HEIGHT_24 = 24
        const val STATUS_BAR_HEIGHT_25 = 25
        const val NAVIGATION_BAR_HEIGHT = 48
    }

    @Before
    fun setUp() {
        ApplicationProvider.getApplicationContext<Context>().injectAsAppCtx()
        display = windowManager.defaultDisplay
        shadowDisplay = Shadows.shadowOf(display)
        shadowDisplay.run {
            setHeight(HEIGHT)
            setWidth(WIDTH)
            setRealHeight(REAL_HEIGHT)
            setRealWidth(REAL_WIDTH)
        }
    }

    @Test
    fun screenHeight_shouldReturnScreenHeight() {
        assertEquals(HEIGHT, screenHeight)
    }

    @Test
    fun screenWidth_shouldReturnScreenWidth() {
        assertEquals(WIDTH, screenWidth)
    }

    @Test
    fun screenRealHeight_shouldReturnScreenRealHeight() {
        assertEquals(REAL_HEIGHT, screenRealHeight)
    }

    @Test
    fun screenRealWidth_shouldReturnScreenRealWidth() {
        assertEquals(REAL_WIDTH, screenRealWidth)
    }

    @Test
    fun navigationBarHeight_shouldReturnNavigationBarHeight() {
        assertEquals(NAVIGATION_BAR_HEIGHT, navigationBarHeight)
    }

    @Test
    fun statusBarHeight_shouldReturnStatusBarHeight() {
        if (Build.VERSION.SDK_INT >= M) {
            assertEquals(STATUS_BAR_HEIGHT_24, statusBarHeight)
        } else {
            assertEquals(STATUS_BAR_HEIGHT_25, statusBarHeight)
        }
    }
}
