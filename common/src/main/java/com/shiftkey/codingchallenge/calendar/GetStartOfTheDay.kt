package com.shiftkey.codingchallenge.calendar

import java.util.*

fun Calendar.getStartOfTheDay(): Calendar {
    val copy = clone() as Calendar
    copy.set(Calendar.HOUR_OF_DAY, 0)
    copy.set(Calendar.MINUTE, 0)
    copy.set(Calendar.SECOND, 0)
    copy.set(Calendar.MILLISECOND, 0)
    return copy
}