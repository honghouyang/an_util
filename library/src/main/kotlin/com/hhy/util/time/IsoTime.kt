package com.hhy.util.time

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("SpellCheckingInspection")
const val ISO_OFFSET_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ"

val isoOffsetDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern(ISO_OFFSET_DATE_TIME)

val utcZoneId: ZoneId = ZoneId.of("UTC")

/** Current iso zoned datetime. */
val isoNow: ZonedDateTime get() = ZonedDateTime.now()

/** Current iso zoned datetime string, e.g. 2021-11-15T17:37:06.560+08:00. */
val isoNowStr: String get() = isoNow.format(isoOffsetDateTimeFormatter)

/**
 * Pair of current iso utc zoned datetime string and zoned offset string.
 *
 * e.g. <2021-11-15T11:01:30.445Z, +08:00>
 */
val isoUtcNowPair: Pair<String, String> get() = isoNow.toIsoUtcPair()

/** Current date. */
val isoDate: LocalDate get() = LocalDate.now()

/** Current date string. */
val isoDateStr: String get() = LocalDate.now().toString()

/**
 *  Convert epoch milliseconds to iso zoned datetime.
 *
 *  @param zoneId target zone id, null means using system default zone.
 */
fun Long.toIsoDateTime(zoneId: String? = null): ZonedDateTime {
    return ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        (if (zoneId == null) ZoneId.systemDefault() else ZoneId.of(zoneId))
    )
}

/**
 * Convert iso zoned datetime string to zoned datetime with specified zone ID.
 *
 * '2021-11-15T11:01:30.445Z' with '+08:00' to ZonedDateTime(2021-11-15T19:01:30.445+08:00)
 *
 * '2021-11-15T19:01:30.445+08:00' with null to ZonedDateTime(2021-11-15T19:01:30.445+08:00)
 *
 * @param zoneId target zone id, null means doesn't change zone.
 */
fun String.toIsoDateTime(zoneId: String? = null): ZonedDateTime {
    return with(ZonedDateTime.parse(this, isoOffsetDateTimeFormatter)) {
        if (zoneId != null) {
            withZoneSameInstant(ZoneId.of(zoneId))
        } else {
            this
        }
    }
}

/**
 * Convert iso zoned datetime string to iso zoned datetime string with specified zone ID.
 *
 * '2021-11-15T11:01:30.445Z' with '+08:00' to '2021-11-15T19:01:30.445+08:00'
 *
 * '2021-11-15T19:01:30.445+08:00' with '+09:00' to '2021-11-15T20:01:30.445+09:00'
 *
 * @param zoneId target zone id, null means doesn't change zone.
 */
fun String.toIsoDateTimeString(zoneId: String? = null): String =
    toIsoDateTime(zoneId).format(isoOffsetDateTimeFormatter)

/**
 * Convert epoch milliseconds to iso zoned datetime string with specified zone ID.
 *
 * @param zoneId target zone id, null means using system default zone.
 */
fun Long.toIsoDateTimeString(zoneId: String? = null): String =
    this.toIsoDateTime(zoneId).format(isoOffsetDateTimeFormatter)

/** Convert iso zoned datetime to epoch milliseconds. */
fun ZonedDateTime.toIsoMillis(): Long = this.toInstant().toEpochMilli()

/** Convert iso zoned datetime string to epoch milliseconds. */
fun String.toIsoMillis(): Long = this.toIsoDateTime().toIsoMillis()

/**
 * Convert iso zoned datetime string to pair of iso utc zoned datetime string and zoned offset string.
 *
 * e.g. '2021-11-15T19:01:30.445+08:00' to <2021-11-15T11:01:30.445Z, +08:00>
 */
fun String.toIsoUtcPair(): Pair<String, String> = toIsoDateTime().toIsoUtcPair()

/**
 * Convert iso zoned datetime to pair of iso utc zoned datetime string and zoned offset string.
 *
 * e.g. ZonedDateTime(2021-11-15T19:01:30.445+08:00) to <2021-11-15T11:01:30.445Z, +08:00>
 */
fun ZonedDateTime.toIsoUtcPair(): Pair<String, String> {
    return Pair(
        isoOffsetDateTimeFormatter.format(withZoneSameInstant(utcZoneId)),
        offset.toString()
    )
}
