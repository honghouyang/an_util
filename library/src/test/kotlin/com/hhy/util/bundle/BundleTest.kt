package com.hhy.util.bundle

import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.delegate.noOpDelegate
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlinx.parcelize.Parcelize
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.Serializable
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@RunWith(AndroidJUnit4::class)
@SmallTest
class BundleTest {
    @Parcelize
    data class Student(val id: String) : Parcelable

    data class Teacher(val id: String) : Serializable

    data class Family(val id: String)

    companion object {
        const val KEY = "key"
        const val STRING_VALUE = "value"
        const val INTEGER_VALUE = 17
        val CHAR_SEQUENCE_VALUE = object : CharSequence by noOpDelegate() {}
        val INTEGER_ARRAY_VALUE = IntArray(4)
        val SHORT_ARRAY_VALUE = ShortArray(5)
        val LONG_ARRAY_VALUE = LongArray(6)
        val BYTE_ARRAY_VALUE = ByteArray(7)
        val FLOAT_ARRAY_VALUE = FloatArray(8)
        val DOUBLE_ARRAY_VALUE = DoubleArray(9)
        val BOOLEAN_ARRAY_VALUE = BooleanArray(9)
        val CHAR_ARRAY_VALUE = CharArray(10)
        val STRING_ARRAY_VALUE = arrayOf<String>()
        val CHAR_SEQUENCE_ARRAY_VALUE = arrayOf<CharSequence>()
        val PARCELABLE_ARRAY_VALUE = arrayOf<Parcelable>()
        val SERIALIZABLE_ARRAY_VALUE = arrayOf<Serializable>()
        val EMPTY_STRING_ARRAY_LIST_VALUE = arrayListOf<String>()
        val STRING_ARRAY_LIST_VALUE = arrayListOf("a", "b", "c")
        val CHAR_SEQUENCE_ARRAY_LIST_VALUE = arrayListOf<CharSequence>("a", "b", "c")
        val INTEGER_ARRAY_LIST_VALUE = arrayListOf(1, 2, 3)
        val PARCELABLE_ARRAY_LIST_VALUE = arrayListOf<Parcelable>(Student("a"), Student("b"))
        val PARCELABLE_SPARSE_ARRAY_VALUE = SparseArray<Parcelable>()
        val BINDER_VALUE = Binder()
        val PARCELABLE_VALUE = Student("a")
        val SERIALIZABLE_VALUE = Teacher("a")
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        PARCELABLE_SPARSE_ARRAY_VALUE.put(1, Student("a"))
        PARCELABLE_SPARSE_ARRAY_VALUE.put(2, Student("b"))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun put_shouldSaveData_whenParamIsString() {
        val bundle = Bundle()
        bundle.put(KEY, STRING_VALUE)
        assertEquals(STRING_VALUE, bundle.getString(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsIntegerArray() {
        val bundle = Bundle()
        bundle.put(KEY, INTEGER_ARRAY_VALUE)
        assertEquals(INTEGER_ARRAY_VALUE, bundle.getIntArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsShortArray() {
        val bundle = Bundle()
        bundle.put(KEY, SHORT_ARRAY_VALUE)
        assertEquals(SHORT_ARRAY_VALUE, bundle.getShortArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsLongArray() {
        val bundle = Bundle()
        bundle.put(KEY, LONG_ARRAY_VALUE)
        assertEquals(LONG_ARRAY_VALUE, bundle.getLongArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsByteArray() {
        val bundle = Bundle()
        bundle.put(KEY, BYTE_ARRAY_VALUE)
        assertEquals(BYTE_ARRAY_VALUE, bundle.getByteArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsFloatArray() {
        val bundle = Bundle()
        bundle.put(KEY, FLOAT_ARRAY_VALUE)
        assertEquals(FLOAT_ARRAY_VALUE, bundle.getFloatArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsDoubleArray() {
        val bundle = Bundle()
        bundle.put(KEY, DOUBLE_ARRAY_VALUE)
        assertEquals(DOUBLE_ARRAY_VALUE, bundle.getDoubleArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsBooleanArray() {
        val bundle = Bundle()
        bundle.put(KEY, BOOLEAN_ARRAY_VALUE)
        assertEquals(BOOLEAN_ARRAY_VALUE, bundle.getBooleanArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsCharArray() {
        val bundle = Bundle()
        bundle.put(KEY, CHAR_ARRAY_VALUE)
        assertEquals(CHAR_ARRAY_VALUE, bundle.getCharArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsCharSequence() {
        val bundle = Bundle()
        bundle.put(KEY, CHAR_SEQUENCE_VALUE)
        assertEquals(CHAR_SEQUENCE_VALUE, bundle.getCharSequence(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsBundle() {
        val bundleX = Bundle()
        bundleX.put(KEY, STRING_VALUE)
        val bundleY = Bundle()
        bundleY.put(KEY, bundleX)
        assertEquals(STRING_VALUE, bundleY.getBundle(KEY)!!.getString(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsArray() {
        val bundle = Bundle()
        bundle.put(KEY, STRING_ARRAY_VALUE)
        assertEquals(STRING_ARRAY_VALUE, bundle.getStringArray(KEY))

        bundle.put(KEY, CHAR_SEQUENCE_ARRAY_VALUE)
        assertEquals(CHAR_SEQUENCE_ARRAY_VALUE, bundle.getCharSequenceArray(KEY))

        bundle.put(KEY, PARCELABLE_ARRAY_VALUE)
        assertEquals(PARCELABLE_ARRAY_VALUE, bundle.getParcelableArray(KEY))

        assertFailsWith<Exception> { bundle.put(KEY, SERIALIZABLE_ARRAY_VALUE) }
    }

    @Test
    fun put_shouldSaveData_whenParamIsArrayList() {
        val bundle = Bundle()
        bundle.put(KEY, STRING_ARRAY_LIST_VALUE)
        assertEquals(STRING_ARRAY_LIST_VALUE, bundle.getStringArrayList(KEY))

        bundle.put(KEY, CHAR_SEQUENCE_ARRAY_LIST_VALUE)
        assertEquals(CHAR_SEQUENCE_ARRAY_LIST_VALUE, bundle.getCharSequenceArrayList(KEY))

        bundle.put(KEY, INTEGER_ARRAY_LIST_VALUE)
        assertEquals(INTEGER_ARRAY_LIST_VALUE, bundle.getIntegerArrayList(KEY))

        bundle.put(KEY, PARCELABLE_ARRAY_LIST_VALUE)
        assertEquals(PARCELABLE_ARRAY_LIST_VALUE, bundle.getParcelableArrayList(KEY))

        bundle.put(KEY, EMPTY_STRING_ARRAY_LIST_VALUE)
        assertEquals(EMPTY_STRING_ARRAY_LIST_VALUE, bundle.getStringArrayList(KEY))

        assertFailsWith<Exception> { bundle.put(KEY, SERIALIZABLE_ARRAY_VALUE) }
    }

    @Test
    fun put_shouldSaveData_whenParamIsSparseArray() {
        val bundle = Bundle()
        bundle.put(KEY, PARCELABLE_SPARSE_ARRAY_VALUE)
        assertEquals(PARCELABLE_SPARSE_ARRAY_VALUE, bundle.getSparseParcelableArray(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsBinder() {
        val bundle = Bundle()
        bundle.put(KEY, BINDER_VALUE)
        assertEquals(BINDER_VALUE, bundle.getBinder(KEY))
    }

    @Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
    @Test
    fun put_shouldSaveData_whenParamIsParcelable() {
        val bundle = Bundle()
        bundle.put(KEY, PARCELABLE_VALUE)
        assertEquals(PARCELABLE_VALUE, bundle.getParcelable(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsSerializable() {
        val bundle = Bundle()
        bundle.put(KEY, SERIALIZABLE_VALUE)
        assertEquals(SERIALIZABLE_VALUE, bundle.getSerializable(KEY))

        bundle.put(KEY, INTEGER_VALUE)
        assertEquals(INTEGER_VALUE, bundle.getInt(KEY))
    }

    @Test
    fun put_shouldSaveData_whenParamIsNull() {
        val bundle = Bundle()
        bundle.put(KEY, null)
        assertNull(bundle.getString(KEY))
    }

    @Test
    fun put_shouldThrowException_whenParamIsOther() {
        val bundle = Bundle()
        assertFailsWith<java.lang.Exception> {
            bundle.put(KEY, Family("a"))
        }
    }
}
