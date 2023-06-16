package com.hhy.util.resource

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@SmallTest
class IdentifierResourcesTest {
    companion object {
        const val IDENTIFIER = 10
        const val NAME = "name"
        const val DEF_TYPE = "def_type"
        const val DEF_PACKAGE = "def_package"
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
    fun identifier_shouldReturnIdentifier_whenInContext() {
        val context = mockk<Context> {
            every { resources.getIdentifier(any(), any(), any()) } returns IDENTIFIER
        }
        assertEquals(IDENTIFIER, context.identifier(NAME, DEF_TYPE, DEF_PACKAGE))
        verify { context.resources.getIdentifier(any(), any(), any()) }
    }

    @Test
    fun identifier_shouldReturnIdentifier_whenInFragment() {
        mockkStatic("com.hhy.util.resource.IdentifierResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { identifier(any(), any(), any()) } returns IDENTIFIER
            }
        }
        val identifier = fragment.identifier(NAME, DEF_TYPE, DEF_PACKAGE)
        assertEquals(IDENTIFIER, identifier)
        verify { fragment.identifier(any(), any(), any()) }
    }

    @Test
    fun identifier_shouldReturnIdentifier_whenInView() {
        mockkStatic("com.hhy.util.resource.IdentifierResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { identifier(any(), any(), any()) } returns IDENTIFIER
            }
        }
        val identifier = view.identifier(NAME, DEF_TYPE, DEF_PACKAGE)
        assertEquals(IDENTIFIER, identifier)
        verify { view.identifier(any(), any(), any()) }
    }

    @Test
    fun identifier_shouldReturnIdentifier_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.IdentifierResourcesKt")
        val context = mockk<Context> {
            every { identifier(any(), any(), any()) } returns IDENTIFIER
        }
        context.injectAsAppCtx()
        val identifier = appIdentifier(NAME, DEF_TYPE, DEF_PACKAGE)
        assertEquals(IDENTIFIER, identifier)
        verify { context.identifier(any(), any(), any()) }
    }
}
