package com.hhy.util.appctx

import android.app.Activity
import android.app.Application
import android.app.Service
import android.app.backup.BackupAgent
import android.content.Context
import android.content.ContextWrapper
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SmallTest
class AppCtxTest {
    @MockK
    lateinit var application: Application

    @MockK
    lateinit var activity: Activity

    @MockK
    lateinit var service: Service

    @MockK
    lateinit var backupAgent: BackupAgent

    @MockK
    lateinit var contextWrapper: ContextWrapper

    @MockK
    lateinit var context: Context

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        internalCtx = null
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun canLeakMemory_shouldReturnFalse_whenContextIsApplication() {
        assertFalse(application.canLeakMemory())
    }

    @Test
    fun canLeakMemory_shouldReturnTrue_whenContextIsActivity() {
        assertTrue(activity.canLeakMemory())
    }

    @Test
    fun canLeakMemory_shouldReturnTrue_whenContextIsService() {
        assertTrue(service.canLeakMemory())
    }

    @Test
    fun canLeakMemory_shouldReturnTrue_whenContextIsBackupAgent() {
        assertTrue(backupAgent.canLeakMemory())
    }

    @Test
    fun canLeakMemory_shouldReturnTrue_whenContextWrapperEqualsBaseContext() {
        every { contextWrapper.baseContext } returns contextWrapper
        assertTrue(contextWrapper.canLeakMemory())
        verify { contextWrapper.baseContext }
    }

    @Test
    fun canLeakMemory_shouldCheckBaseContext_whenContextWrapperNotEqualsBaseContext() {
        every { contextWrapper.baseContext } returns application
        assertFalse(contextWrapper.canLeakMemory())

        every { contextWrapper.baseContext } returns activity
        assertTrue(contextWrapper.canLeakMemory())
    }

    @Test
    fun canLeakMemory_shouldCheckApplicationContext_whenContextIsNotContextWrapper() {
        assertFalse(context.canLeakMemory())
        every { context.applicationContext } returns null
        assertTrue(context.canLeakMemory())
        verify { context.applicationContext }
    }

    @Test
    fun internalCtx_shouldBeInjected_whenContextNotLeak() {
        assertNull(internalCtx)
        application.injectAsAppCtx()
        assertNotNull(internalCtx)
        assertEquals(application, internalCtx)
    }

    @Test
    fun internalCtx_shouldNotBeInjected_whenContextLeak() {
        assertNull(internalCtx)
        assertFails { activity.injectAsAppCtx() }
        assertNull(internalCtx)
    }

    @Test
    fun appCtx_shouldBeInternalCtx_whenInternalCtxNotNull() {
        internalCtx = application
        assertNotNull(appCtx)
        assertEquals(internalCtx, appCtx)
    }

    @Test
    fun appCtx_shouldBeInject_whenInternalCtxNull() {
        mockkStatic("com.hhy.util.appctx.AppCtx")
        assertNull(internalCtx)
        every { initAndGetAppCtxWithReflection() } returns application
        assertNotNull(appCtx)
        assertEquals(application, appCtx)
    }
}
