package com.hhy.util.time

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

private const val DEPRECATED_DESCRIPTION = "using iso prefix properties or functions"
private const val DEPRECATED_REPLACE_IMPORT = "com.hhy.util.time"

@Suppress("SpellCheckingInspection")
@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("ISO_OFFSET_DATE_TIME", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
const val FMT_STANDARD = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("isoOffsetDateTimeFormatter", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
val standardFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(FMT_STANDARD)

/** Current ISO time. */
@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("isoNow", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
val now: OffsetDateTime
    get() = OffsetDateTime.now()

/** Current ISO time string, e.g. 2019-05-08T16:27:33.881+0800. */
@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("isoNowStr", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
val nowStr: String
    get() = now.format(standardFormatter)

/** Current date. */
@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("isoDate", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
val date: LocalDate
    get() = LocalDate.now()

/** Current date string. */
@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("isoDateStr", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
val dateStr: String
    get() = LocalDate.now().toString()

@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("toIsoDateTime", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
fun Long.toDateTime(): OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())

@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("toIsoDateTime", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
fun String.toDateTime(): OffsetDateTime = OffsetDateTime.parse(this, standardFormatter)

@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("toIsoMillis", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
fun OffsetDateTime.toMillis(): Long = this.toInstant().toEpochMilli()

@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("toIsoMillis", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
fun String.toMillis(): Long = this.toDateTime().toMillis()

@Deprecated(
    DEPRECATED_DESCRIPTION,
    ReplaceWith("toIsoDateTimeString", DEPRECATED_REPLACE_IMPORT),
    DeprecationLevel.WARNING
)
fun Long.toDateTimeString(): String = this.toDateTime().format(standardFormatter)
