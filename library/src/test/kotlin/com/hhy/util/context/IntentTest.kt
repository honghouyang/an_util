package com.hhy.util.context

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SmallTest
class IntentTest {
    companion object {
        private const val PACKAGE_NAME = "com.hhy.util.context"
        private const val ACTIVITY_NAME = "TargetActivity"
    }

    @MockK
    lateinit var application: Application

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        application.injectAsAppCtx()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun toExplicitServiceIntent_shouldReturnNull_whenNoExist() {
        val intent = Intent()
        assertNull(intent.toExplicitServiceIntent())
    }

    @Test
    fun toExplicitServiceIntent_shouldReturnServiceIntent_whenServiceExist() {
        val pm = mockk<PackageManager>()
        every { appCtx.packageManager } returns pm
        every { pm.queryIntentServices(any(), any()) } returns mutableListOf(
            mockk {
                serviceInfo = mockk {
                    packageName = PACKAGE_NAME
                    name = ACTIVITY_NAME
                }
            }
        )

        val intent = Intent()
        val serviceIntent = intent.toExplicitServiceIntent()

        assertNotNull(serviceIntent)
    }
}
