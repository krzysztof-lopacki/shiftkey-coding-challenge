package com.shiftkey.codingchallenge.availableshiftsbrowser.remote.shifts

import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Day
import com.shiftkey.codingchallenge.availableshiftsbrowser.remote.dto.Response
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

internal interface ShiftsApi {
    companion object {
        private const val AVAILABLE_SHIFTS = "available_shifts"
        private const val AVAILABLE_SHIFTS_PARAMS_DATE = "start"
        private const val AVAILABLE_SHIFTS_PARAMS_ADDRESS = "address"
        private const val AVAILABLE_SHIFTS_PARAMS_MODE = "type"
        private const val AVAILABLE_SHIFTS_PARAMS_MODE_WEEK = "week"

        const val API_NAME = "ShiftsApi"
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val DATE_AND_TIME_WITH_TIMEZONE = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US)
    }

    @GET(AVAILABLE_SHIFTS)
    fun getAvailableShifts(
        @Query(AVAILABLE_SHIFTS_PARAMS_DATE) date: String,
        @Query(AVAILABLE_SHIFTS_PARAMS_ADDRESS) address: String,
        @Query(AVAILABLE_SHIFTS_PARAMS_MODE) span: String = AVAILABLE_SHIFTS_PARAMS_MODE_WEEK
    ): Single<Response<Day>>
}