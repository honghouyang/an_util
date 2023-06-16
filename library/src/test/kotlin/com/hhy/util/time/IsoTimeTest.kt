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
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class IsoTimeTest {
    companion object {
        const val CURRENT_DATE_TIME = "2020-10-20T19:45:30.007+08:00"
        const val SPECIFIED_DATE_TIME = "2020-10-20T20:45:30.007+09:00"
        const val CURRENT_UTC_DATE_TIME = "2020-10-20T11:45:30.007Z"
        const val CURRENT_UTC_OFFSET = "+08:00"
        const val SPECIFIED_UTC_OFFSET = "+09:00"
        const val CURRENT_DATE = "2020-10-20"
        const val CURRENT_MILLIS = 1603194330007
    }

    private lateinit var refDateTime: ZonedDateTime
    private lateinit var refUtcDateTime: ZonedDateTime
    private lateinit var refDate: LocalDate

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        AndroidThreeTen.init(appCtx)
        refDateTime = ZonedDateTime.parse(
            CURRENT_DATE_TIME,
            isoOffsetDateTimeFormatter
        )
        refUtcDateTime = ZonedDateTime.parse(
            CURRENT_UTC_DATE_TIME,
            isoOffsetDateTimeFormatter
        )
        refDate = LocalDate.parse(
            CURRENT_DATE,
            DateTimeFormatter.ISO_LOCAL_DATE
        )
        mockkStatic(ZonedDateTime::class)
        every { ZonedDateTime.now() } returns refDateTime
        mockkStatic(LocalDate::class)
        every { LocalDate.now() } returns refDate
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun isoNow_shouldReturnCurrentZonedDateTime() {
        // given
        val now = isoNow

        // then
        verify { ZonedDateTime.now() }
        assertEquals(refDateTime, now)
    }

    @Test
    fun isoNowStr_shouldReturnCurrentIsoDateTimeString() {
        // given
        val isoNowStr = isoNowStr

        // then
        verify { ZonedDateTime.now() }
        assertEquals(CURRENT_DATE_TIME, isoNowStr)
    }

    @Test
    fun isoUtcNowPair_shouldReturnCurrentUtcAndOffsetPair() {
        // given
        val pair = isoUtcNowPair

        // then
        verify { ZonedDateTime.now() }
        assertEquals(CURRENT_UTC_DATE_TIME, pair.first)
        assertEquals(CURRENT_UTC_OFFSET, pair.second)
    }

    @Test
    fun isoDate_shouldReturnCurrentLocalDate() {
        // given
        val date = isoDate

        // then
        verify { LocalDate.now() }
        assertEquals(refDate, date)
    }

    @Test
    fun isoDateStr_shouldReturnCurrentLocalDateString() {
        // given
        val dateStr = isoDateStr

        // then
        verify { LocalDate.now() }
        assertEquals(CURRENT_DATE, dateStr)
    }

    @Test
    fun longToIsoDateTime_shouldReturnDefaultZonedDateTime_whenWithoutZoneId() {
        // given
        val dateTime = CURRENT_MILLIS.toIsoDateTime()

        // then
        assertEquals(CURRENT_DATE_TIME, dateTime.format(isoOffsetDateTimeFormatter))
    }

    @Test
    fun longToIsoDateTime_shouldReturnSpecifiedZonedDateTime_whenWithSpecifiedZoneId() {
        // given
        val dateTime = CURRENT_MILLIS.toIsoDateTime(SPECIFIED_UTC_OFFSET)

        // then
        assertEquals(SPECIFIED_DATE_TIME, dateTime.format(isoOffsetDateTimeFormatter))
    }

    @Test
    fun stringToIsoDateTime_shouldReturnDefaultZonedDateTime_whenWithoutZoneId() {
        // given
        val dateTime = CURRENT_DATE_TIME.toIsoDateTime()

        // then
        assertEquals(CURRENT_DATE_TIME, dateTime.format(isoOffsetDateTimeFormatter))
    }

    @Test
    fun stringToIsoDateTime_shouldReturnSpecifiedZonedDateTime_whenWithSpecifiedZoneId() {
        // given
        val dateTime = CURRENT_DATE_TIME.toIsoDateTime(SPECIFIED_UTC_OFFSET)

        // then
        assertEquals(SPECIFIED_DATE_TIME, dateTime.format(isoOffsetDateTimeFormatter))
    }

    @Test
    fun stringToIsoDateTimeString_shouldReturnOriginalIsoDateTimeString_whenWithoutZoneId() {
        // given
        val dateTimeStr = CURRENT_UTC_DATE_TIME.toIsoDateTimeString()

        // then
        assertEquals(CURRENT_UTC_DATE_TIME, dateTimeStr)
    }

    @Test
    fun stringToIsoDateTimeString_shouldReturnTargetIsoDateTimeString_whenWithSpecifiedZoneId() {
        // given
        val dateTimeStr = CURRENT_UTC_DATE_TIME.toIsoDateTimeString(CURRENT_UTC_OFFSET)

        // then
        assertEquals(CURRENT_DATE_TIME, dateTimeStr)
    }

    @Test
    fun longToIsoDateTimeString_shouldReturnDefaultZonedDateTime_whenWithoutZoneId() {
        // given
        val dateTimeStr = CURRENT_MILLIS.toIsoDateTimeString()

        // then
        assertEquals(CURRENT_DATE_TIME, dateTimeStr)
    }

    @Test
    fun longToIsoDateTimeString_shouldReturnSpecifiedIsoDateTimeString_whenWithSpecifiedZoneId() {
        // given
        val dateTimeStr = CURRENT_MILLIS.toIsoDateTimeString(SPECIFIED_UTC_OFFSET)

        // then
        assertEquals(SPECIFIED_DATE_TIME, dateTimeStr)
    }

    @Test
    fun zonedDateTimeToMillis_shouldReturnRightMillis() {
        // given
        val millis = refDateTime.toIsoMillis()

        // then
        assertEquals(CURRENT_MILLIS, millis)
    }

    @Test
    fun stringToMillis_shouldReturnRightMillis() {
        // given
        val millis = CURRENT_DATE_TIME.toIsoMillis()

        // then
        assertEquals(CURRENT_MILLIS, millis)
    }

    @Test
    fun stringToIsoUtcPair_shouldReturnOppositeUtcAndOffsetPair() {
        // given
        val pair = SPECIFIED_DATE_TIME.toIsoUtcPair()

        // then
        assertTrue {
            CURRENT_UTC_DATE_TIME == pair.first && SPECIFIED_UTC_OFFSET == pair.second
        }
    }

    @Test
    fun zonedDateTimeToIsoUtcPair_shouldReturnOppositeUtcAndOffsetPair() {
        // given
        val pair = refDateTime.toIsoUtcPair()

        // then
        assertTrue {
            CURRENT_UTC_DATE_TIME == pair.first && CURRENT_UTC_OFFSET == pair.second
        }
    }
}
