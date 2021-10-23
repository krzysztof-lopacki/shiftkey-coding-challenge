package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.models

import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Day
import com.shiftkey.codingchallenge.log.formatCalendar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private val DAY_DIVIDER_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM)

sealed class ShiftsListItem {
    data class DaysSeparatorItem(
        val date: String
    ) : ShiftsListItem() {
        companion object {
            fun valueOf(day: Day): DaysSeparatorItem {
                return DaysSeparatorItem(DAY_DIVIDER_DATE_FORMAT.formatCalendar(day.date))
            }
        }
    }

    data class ShiftItem (
        val shift: Shift
    ) : ShiftsListItem() {
        companion object {
            fun valueOf(shift: com.shiftkey.codingchallenge.availableshiftsbrowser.models.Shift): ShiftItem {
                return ShiftItem(Shift.valueOf(shift))
            }
        }
    }
}

