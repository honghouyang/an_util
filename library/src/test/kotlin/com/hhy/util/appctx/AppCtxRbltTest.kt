package com.hhy.util.appctx

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppCtxRbltTest {
    @Before
    fun setUp() {
        internalCtx = null
    }

    @Test
    fun internalCtx_shouldNotBeInjected_whenReflection() {
        assertNull(internalCtx)
        val context = initAndGetAppCtxWithReflection()
        assertNotNull(internalCtx)
        assertEquals(context, internalCtx)
    }
}
