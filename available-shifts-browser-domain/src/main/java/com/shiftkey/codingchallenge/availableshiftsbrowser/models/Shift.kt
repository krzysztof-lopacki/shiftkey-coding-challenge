package com.shiftkey.codingchallenge.availableshiftsbrowser.models

import com.shiftkey.codingchallenge.log.toLogString
import java.util.*

data class Shift(
    val shiftId: Long,
    val startTime: Calendar,
    val endTime: Calendar,
    val normalizedStartDateTime: String,
    val normalizedEndDateTime: String,
    val timezone: TimeZone,
    val premiumRate: Boolean,
    val covid: Boolean,
    val shiftKind: String,
    val withinDistance: Int,
    val facilityType: FacilityType,
    val skill: Skill,
    val localizedSpecialty: LocalizedSpecialty
) {
    override fun toString(): String {
        return "ShiftDto(shiftId=$shiftId, startTime=${startTime.toLogString()}, endTime=${endTime.toLogString()}, normalizedStartDateTime='$normalizedStartDateTime', normalizedEndDateTime='$normalizedEndDateTime', timezone='$timezone', premiumRate=$premiumRate, covid=$covid, shiftKind='$shiftKind', withinDistance=$withinDistance, facilityType=$facilityType, skillDto=$skill, localizedSpecialty=$localizedSpecialty)"
    }
}