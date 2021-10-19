package com.shiftkey.codingchallenge.log

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private val DATE_AND_TIME = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

fun Calendar.toLogString(): String {
    DATE_AND_TIME.timeZone = timeZone
    return "${DATE_AND_TIME.format(time)} (${timeZone.id})"
}

fun DateFormat.formatCalendar(calendar: Calendar): String {
    timeZone = calendar.timeZone
    return format(calendar.time)
}