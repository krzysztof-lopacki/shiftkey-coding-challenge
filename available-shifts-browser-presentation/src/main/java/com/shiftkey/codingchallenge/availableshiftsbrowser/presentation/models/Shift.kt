package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.models

import android.graphics.Color
import android.os.Parcelable
import com.shiftkey.codingchallenge.calendar.getStartOfTheDay
import com.shiftkey.codingchallenge.log.formatCalendar
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private val SHIFT_DATE_FORMAT = SimpleDateFormat("EEEE", Locale.getDefault())
private val SHIFT_FULL_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG)
private val SHIFT_TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT)

@Parcelize
class Shift (
    val shiftId: Long,
    val day: String,
    val date: String,
    val startTime: String,
    val endTime: String,

    val skill: String,
    val specialty: String,
    val specialtyColor: Int,

    val distance: Int,
    val facilityType: String,
    val facilityColor: Int
) : Parcelable {
    companion object {
        fun valueOf(shift: com.shiftkey.codingchallenge.availableshiftsbrowser.models.Shift): Shift {
            return Shift(
                shiftId = shift.shiftId,
                day = SHIFT_DATE_FORMAT.formatCalendar(shift.startTime.getStartOfTheDay()),
                date = SHIFT_FULL_DATE_FORMAT.formatCalendar(shift.startTime.getStartOfTheDay()),
                startTime = SHIFT_TIME_FORMAT.formatCalendar(shift.startTime),
                endTime = SHIFT_TIME_FORMAT.formatCalendar(shift.endTime),

                skill = shift.skill.name,
                specialty = shift.localizedSpecialty.name,
                specialtyColor = Color.parseColor(shift.localizedSpecialty.specialty.color),

                facilityType = shift.facilityType.name,
                facilityColor = Color.parseColor(shift.facilityType.color),
                distance = shift.withinDistance
            )
        }
    }
}