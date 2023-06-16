package com.hhy.util.preference

import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@SmallTest
class ResettableLazyTest {
    companion object {
        const val CONTENT1 = "Hello, Kotlin!"
        const val CONTENT2 = "Bye, Java!"
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
    fun resettableLazy_shouldAutoInitValue_whenValueIsNull() {
        val contentDelegate = resettableLazy {
            CONTENT1
        }
        var content: String by contentDelegate
        assertEquals(CONTENT1, content)

        content = CONTENT2
        assertEquals(CONTENT2, content)

        contentDelegate.reset()
        assertEquals(CONTENT1, content)
    }
}
