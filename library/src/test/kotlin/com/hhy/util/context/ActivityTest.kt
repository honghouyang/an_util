package com.hhy.util.context

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
@SmallTest
class ActivityTest {
    companion object {
        private const val PACKAGE_NAME = "com.hhy.util.context"
        private const val ACTIVITY_NAME = "com.hhy.util.context.TargetActivity"
        private const val KEY = "name"
        private const val VALUE = "kotlin"
    }

    @MockK
    lateinit var context: Activity

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun startActivity_shouldAutoCreateIntent_whenUsingGenericTypes() {
        context.startActivity<TargetActivity>()

        val slot = slot<Intent>()
        verify { context.startActivity(capture(slot)) }

        val intent = slot.captured
        assertNotNull(intent.component)
        assertEquals(TargetActivity::class.java.name, intent.component?.className)
    }

    @Test
    fun startActivity_shouldWorks_whenWithExtras() {
        val extras = Bundle()
        extras.putString(KEY, VALUE)
        context.startActivity<TargetActivity>(extras)

        val slot = slot<Intent>()
        verify { context.startActivity(capture(slot)) }

        val intent = slot.captured
        assertNotNull(intent.component)
        assertEquals(VALUE, intent.extras!![KEY])
    }

    @Test
    fun startActivity_shouldWorks_whenWithConfigIntent() {
        context.startActivity<TargetActivity> {
            putExtra(KEY, VALUE)
        }

        val slot = slot<Intent>()
        verify { context.startActivity(capture(slot)) }

        val intent = slot.captured
        assertNotNull(intent.component)
        assertEquals(VALUE, intent.extras!![KEY])
    }

    @Test
    fun startActivityMoreConfigs_shouldWorks_whenWithActivityName() {
        context.startActivity(activityName = ACTIVITY_NAME)

        val slot = slot<Intent>()
        verify { context.startActivity(capture(slot)) }

        val intent = slot.captured
        assertNotNull(intent.component)
        assertEquals(TargetActivity::class.java.canonicalName, intent.component!!.className)
    }

    @Test
    fun startActivityMoreConfigs_shouldWorks_whenWithPkgName() {
        context.startActivity(
            packageName = PACKAGE_NAME,
            activityName = ACTIVITY_NAME
        )

        val slot = slot<Intent>()
        verify { context.startActivity(capture(slot)) }

        val intent = slot.captured
        assertNotNull(intent.component)
        assertEquals(TargetActivity::class.java.canonicalName, intent.component!!.className)
    }

    @Test
    fun startActivityMoreConfigs_shouldWorks_whenWithExtras() {
        val extras = Bundle()
        extras.putString(KEY, VALUE)
        context.startActivity(activityName = ACTIVITY_NAME, extras = extras)

        val slot = slot<Intent>()
        verify { context.startActivity(capture(slot)) }

        val intent = slot.captured
        assertNotNull(intent.component)
        assertEquals(VALUE, intent.extras!![KEY])
    }

    @Test
    fun startActivityMoreConfigs_shouldWorks_whenUsingSingleTask() {
        context.startActivity(activityName = ACTIVITY_NAME, singleTask = true)

        val slot = slot<Intent>()
        verify { context.startActivity(capture(slot)) }

        val intent = slot.captured
        assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP, intent.flags)
    }

    @Test
    fun startActivityMoreConfigs_shouldWorks_whenWithConfigIntent() {
        context.startActivity(activityName = ACTIVITY_NAME) {
            putExtra(KEY, VALUE)
        }

        val slot = slot<Intent>()
        verify { context.startActivity(capture(slot)) }

        val intent = slot.captured
        assertNotNull(intent.component)
        assertEquals(VALUE, intent.extras!![KEY])
    }
}

class TargetActivity : Activity()
