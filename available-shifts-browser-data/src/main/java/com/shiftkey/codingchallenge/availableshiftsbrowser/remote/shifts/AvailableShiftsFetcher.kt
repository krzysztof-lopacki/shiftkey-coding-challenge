package com.shiftkey.codingchallenge.availableshiftsbrowser.remote.shifts

import com.shiftkey.codingchallenge.availableshiftsbrowser.config.Configuration
import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Day
import com.shiftkey.codingchallenge.calendar.getStartOfTheDay
import com.shiftkey.codingchallenge.log.formatCalendar
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AvailableShiftsFetcher @Inject constructor(
    configuration: Configuration,
    okHttpClient: OkHttpClient,
    @Named(ShiftsApi.API_NAME) responseConverter: Converter.Factory
) {
    private val shiftsApi: ShiftsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(configuration.shiftsApiUrl)
            .addConverterFactory(responseConverter)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(okHttpClient)
            .build()
        shiftsApi = retrofit.create(ShiftsApi::class.java)
    }

    fun getAvailableShiftsForAWeek(
        firstDayToFetch: Calendar,
        address: String
    ): Single<List<Day>> {
        val firstDayString = ShiftsApi.DATE_FORMAT.formatCalendar(firstDayToFetch.getStartOfTheDay())
        return shiftsApi
            .getAvailableShifts(
                date = firstDayString,
                address = address
            )
            .map { response ->
                response.data
                    .filter { day -> day.shifts.isNotEmpty() }
                    .map { day ->
                        day.shifts.onEach { shift ->
                            shift.startTime.timeZone = shift.timezone
                            shift.endTime.timeZone = shift.timezone
                        }
                        Day(
                            // Day DTO does not contain the timezone of the date, so we need to use first shift's start time instead
                            day.shifts.first().startTime.getStartOfTheDay(),
                            day.shifts
                        )
                    }
            }
    }
}