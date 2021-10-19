package com.shiftkey.codingchallenge.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.DateFormat
import java.util.*

class CalendarDeserializer(
    private vararg val dateFormats: DateFormat
) : JsonDeserializer<Calendar> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Calendar? {
        val dateString = json!!.asJsonPrimitive.asString
        if (dateString.isNullOrEmpty()) return null
        for (dateFormat in dateFormats) {
            try {
                return Calendar.getInstance().apply { time = dateFormat.parse(dateString)!! }
            } catch (e: Exception) { }
        }
        throw JsonParseException("Cannot parse $dateString to Calendar - invalid date format.")
    }
}