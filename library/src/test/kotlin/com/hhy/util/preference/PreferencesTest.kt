package com.hhy.util.preference

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.exception.unsupported
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class PreferencesTest {
    companion object {
        const val PREFS_NAME = "game_state"
        const val BOOL_DEFAULT_VALUE = true
        const val BOOL_EDITED_VALUE = false
        const val INT_DEFAULT_VALUE = 1
        const val INT_EDITED_VALUE = 2
        const val FLOAT_DEFAULT_VALUE = 1F
        const val FLOAT_EDITED_VALUE = 2F
        const val LONG_DEFAULT_VALUE = 1L
        const val LONG_EDITED_VALUE = 2L
        const val STRING_DEFAULT_VALUE = "kotlin"
        const val STRING_EDITED_VALUE = "java"
        val STRING_SET_DEFAULT_VALUE = setOf("a", "b", "c")
        val STRING_SET_EDITED_VALUE = setOf("c", "b", "a")
    }

    object GamePreferences : Preferences(PREFS_NAME) {
        var a1 by boolPref()
        var a2 by boolPref(BOOL_DEFAULT_VALUE)
        var b by intPref(INT_DEFAULT_VALUE)
        var c by floatPref(FLOAT_DEFAULT_VALUE)
        var d by longPref(LONG_DEFAULT_VALUE)
        var e by stringPref(STRING_DEFAULT_VALUE)
        var f1 by stringOrNullPref()
        var f2 by stringOrNullPref(STRING_DEFAULT_VALUE)
        var g by stringSetPref(STRING_SET_DEFAULT_VALUE)
        var h1 by stringSetOrNullPref()
        var h2 by stringSetOrNullPref(STRING_SET_DEFAULT_VALUE)

        var i by StringPref(STRING_DEFAULT_VALUE, STRING_DEFAULT_VALUE)
    }

    @Before
    fun setUp() {
        ApplicationProvider.getApplicationContext<Context>().injectAsAppCtx()
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun preferences_shouldUseDefaultData_whenInit() {
        assertFalse(GamePreferences.a1)
        assertTrue(GamePreferences.a2)
        assertEquals(INT_DEFAULT_VALUE, GamePreferences.b)
        assertEquals(FLOAT_DEFAULT_VALUE, GamePreferences.c)
        assertEquals(LONG_DEFAULT_VALUE, GamePreferences.d)
        assertEquals(STRING_DEFAULT_VALUE, GamePreferences.e)
        assertNull(GamePreferences.f1)
        assertEquals(STRING_DEFAULT_VALUE, GamePreferences.f2)
        assertEquals(STRING_SET_DEFAULT_VALUE, GamePreferences.g)
        assertNull(GamePreferences.h1)
        assertEquals(STRING_SET_DEFAULT_VALUE, GamePreferences.h2)
    }

    @Test
    fun preferences_shouldUseSavedData_whenEdited() {
        GamePreferences.a1 = BOOL_EDITED_VALUE
        assertEquals(BOOL_EDITED_VALUE, GamePreferences.a1)

        GamePreferences.b = INT_EDITED_VALUE
        assertEquals(INT_EDITED_VALUE, GamePreferences.b)

        GamePreferences.c = FLOAT_EDITED_VALUE
        assertEquals(FLOAT_EDITED_VALUE, GamePreferences.c)

        GamePreferences.d = LONG_EDITED_VALUE
        assertEquals(LONG_EDITED_VALUE, GamePreferences.d)

        GamePreferences.e = STRING_EDITED_VALUE
        assertEquals(STRING_EDITED_VALUE, GamePreferences.e)

        GamePreferences.f1 = STRING_EDITED_VALUE
        assertEquals(STRING_EDITED_VALUE, GamePreferences.f1)

        GamePreferences.g = STRING_SET_EDITED_VALUE
        assertEquals(STRING_SET_EDITED_VALUE, GamePreferences.g)

        GamePreferences.h1 = STRING_SET_EDITED_VALUE
        assertEquals(STRING_SET_EDITED_VALUE, GamePreferences.h1)

        GamePreferences.i = STRING_EDITED_VALUE
        assertEquals(STRING_EDITED_VALUE, GamePreferences.i)
    }

    @Test
    fun preferences_shouldSupportBatchEdit_whenInEditBlock() {
        GamePreferences.a1 = BOOL_EDITED_VALUE
        assertEquals(BOOL_EDITED_VALUE, GamePreferences.a1)

        GamePreferences.b = INT_EDITED_VALUE
        assertEquals(INT_EDITED_VALUE, GamePreferences.b)

        GamePreferences.edit {
            a1 = BOOL_DEFAULT_VALUE
            b = INT_DEFAULT_VALUE
        }

        assertTrue(GamePreferences.a1)
        assertEquals(INT_DEFAULT_VALUE, GamePreferences.b)
    }

    @Test
    fun editor_shouldBeRecreated_whenAbortEdit() {
        val editorA = GamePreferences.editor
        val editorB = GamePreferences.editor
        assertSame(editorA, editorB)
        try {
            GamePreferences.edit {
                unsupported()
            }
        } catch (e: Exception) {
            // ignored
        }

        val editorC = GamePreferences.editor
        assertNotSame(editorA, editorC)
    }

    @Test
    fun contains_shouldTellsContainPrefsOrNot() {
        assertTrue {
            GamePreferences.contains(
                appCtx.getSharedPreferences(
                    PREFS_NAME,
                    Context.MODE_PRIVATE
                )
            )
        }
        assertFalse {
            GamePreferences.contains(
                appCtx.getSharedPreferences(
                    "not_exist",
                    Context.MODE_PRIVATE
                )
            )
        }
    }
}
