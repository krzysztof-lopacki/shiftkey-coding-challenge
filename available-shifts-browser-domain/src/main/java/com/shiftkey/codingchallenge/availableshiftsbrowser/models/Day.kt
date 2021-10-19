package com.shiftkey.codingchallenge.availableshiftsbrowser.models

import com.shiftkey.codingchallenge.log.toLogString
import java.util.*

data class Day(
    val date: Calendar,
    val shifts: List<Shift>
) {
    override fun toString(): String {
        return "Day(date=${date.toLogString()}, shifts=$shifts)"
    }
}