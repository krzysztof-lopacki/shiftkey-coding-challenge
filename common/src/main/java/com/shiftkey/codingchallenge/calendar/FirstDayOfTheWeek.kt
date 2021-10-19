package com.shiftkey.codingchallenge.calendar

import java.util.*

fun Calendar.getFirstDayOfThisWeek(): Calendar {
    val firstDayOfThisWeek = clone() as Calendar
    firstDayOfThisWeek.add(Calendar.DAY_OF_WEEK, firstDayOfWeek - get(Calendar.DAY_OF_WEEK))
    return firstDayOfThisWeek
}

fun Calendar.getFirstDayOfTheNextWeek(): Calendar {
    val firstDayOfTheNextWeek = getFirstDayOfThisWeek()
    firstDayOfTheNextWeek.add(Calendar.DATE, 7)
    return firstDayOfTheNextWeek
}

