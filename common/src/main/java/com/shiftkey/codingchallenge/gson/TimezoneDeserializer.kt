package com.shiftkey.codingchallenge.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

private const val US_CENTRAL_TIMEZONE_ALIAS: String = "Central"
private val US_CENTRAL_TIMEZONE: TimeZone = TimeZone.getTimeZone("US/Central")

class TimezoneDeserializer(
    private vararg val dateFormats: TimeZone
) : JsonDeserializer<TimeZone> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TimeZone? {
        val timezoneId = json!!.asJsonPrimitive.asString
        if (timezoneId.isNullOrEmpty()) return null
        if (timezoneId == US_CENTRAL_TIMEZONE_ALIAS) {
            // ShiftsAPI is US-centric and as result uses wrong, non-standard timezone identifiers.
            // In fact 'Central' timezone is the alias of 'GMT' timezone. 'US/Central' is the correct id.
            return US_CENTRAL_TIMEZONE
        } else {
            try {
                return TimeZone.getTimeZone(timezoneId)
            } catch (e: Exception) { }
        }
        throw JsonParseException("Cannot parse $timezoneId to Calendar - invalid date format.")
    }
}