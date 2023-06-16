package com.hhy.util.time

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.jakewharton.threetenabp.AndroidThreeTen
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@SmallTest
class TimeTest {
    companion object {
        const val CURRENT_DATE_TIME = "2020-10-20T19:45:30.007+0800"
        const val CURRENT_DATE = "2020-10-20"
        const val CURRENT_MILLIS = 1603194330007
    }

    private lateinit var refDateTime: OffsetDateTime
    private lateinit var refDate: LocalDate

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        AndroidThreeTen.init(appCtx)
        refDateTime = OffsetDateTime.parse(
            CURRENT_DATE_TIME,
            standardFormatter
        )
        refDate = LocalDate.parse(
            CURRENT_DATE,
            DateTimeFormatter.ISO_LOCAL_DATE
        )
        mockkStatic(OffsetDateTime::class)
        every { OffsetDateTime.now() } returns refDateTime
        mockkStatic(LocalDate::class)
        every { LocalDate.now() } returns refDate
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun now_shouldReturnCurrentOffsetDateTime() {
        val now = now

        verify { OffsetDateTime.now() }
        assertEquals(refDateTime, now)
    }

    @Test
    fun nowStr_shouldReturnCurrentOffsetDateTimeInString() {
        val nowStr = nowStr

        verify { OffsetDateTime.now() }
        assertEquals(CURRENT_DATE_TIME, nowStr)
    }

    @Test
    fun date_shouldReturnCurrentLocalDate() {
        val date = date

        verify { LocalDate.now() }
        assertEquals(refDate, date)
    }

    @Test
    fun dateStr_shouldReturnCurrentLocalDateInString() {
        val dateStr = dateStr

        verify { LocalDate.now() }
        assertEquals(CURRENT_DATE, dateStr)
    }

    @Test
    fun longToDateTime_shouldReturnSpecificOffsetDateTime() {
        val dateTime = CURRENT_MILLIS.toDateTime()
        assertEquals(refDateTime, dateTime)
    }

    @Test
    fun offsetDateTimeToMillis_shouldReturnRightMillis() {
        assertEquals(CURRENT_MILLIS, refDateTime.toMillis())
    }

    @Test
    fun stringToOffsetDateTime_shouldWorks() {
        val dateTime = CURRENT_DATE_TIME.toDateTime()
        assertEquals(refDateTime, dateTime)
    }

    @Test
    fun stringToMillis_shouldWorks() {
        assertEquals(CURRENT_MILLIS, CURRENT_DATE_TIME.toMillis())
    }

    @Test
    fun millsToDateTimeString_shouldWorks() {
        assertEquals(CURRENT_DATE_TIME, CURRENT_MILLIS.toDateTimeString())
    }
}
