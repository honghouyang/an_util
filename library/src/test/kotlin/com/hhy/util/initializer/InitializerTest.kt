package com.hhy.util.initializer

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.internalCtx
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.SpyK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(AndroidJUnit4::class)
@SmallTest
class InitializerTest {
    @SpyK
    var appCtxInitializer = AppCtxInitializer()
    var threeTenAbpInitializer = ThreeTenAbpInitializer()

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
    fun internalCtx_shouldInit_whenOnCreateInvoked() {
        // given
        assertNull(internalCtx)
        // when
        appCtxInitializer.create(ApplicationProvider.getApplicationContext())
        // then
        assertNotNull(internalCtx)
        assertEquals(ApplicationProvider.getApplicationContext(), internalCtx)
    }

    @Test
    fun threeTenAbp_shouldInit_whenOnCreateInvoked() {
        // given
        assertNull(internalCtx)
        // when
        threeTenAbpInitializer.create(ApplicationProvider.getApplicationContext())
        // then
        assertNotNull(OffsetDateTime.now())
    }
}
