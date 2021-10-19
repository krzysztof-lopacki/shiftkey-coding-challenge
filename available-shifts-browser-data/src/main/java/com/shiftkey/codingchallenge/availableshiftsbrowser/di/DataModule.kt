package com.shiftkey.codingchallenge.availableshiftsbrowser.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.shiftkey.codingchallenge.availableshiftsbrowser.remote.shifts.ShiftsApi
import com.shiftkey.codingchallenge.availableshiftsbrowser.repository.AvailableShiftsRepository
import com.shiftkey.codingchallenge.availableshiftsbrowser.repository.RetrofitBasedAvailableShiftsRepository
import com.shiftkey.codingchallenge.gson.CalendarDeserializer
import com.shiftkey.codingchallenge.gson.TimezoneDeserializer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindAvailableShiftsRepository(implementation: RetrofitBasedAvailableShiftsRepository): AvailableShiftsRepository

    companion object {
        @Singleton
        @Provides
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

        @Singleton
        @Provides
        @Named(ShiftsApi.API_NAME)
        fun provideShiftKeyApiConverter(): Converter.Factory = GsonConverterFactory.create(
            GsonBuilder()
                .registerTypeAdapter(
                    Calendar::class.java,
                    CalendarDeserializer(
                        ShiftsApi.DATE_AND_TIME_WITH_TIMEZONE,
                        ShiftsApi.DATE_FORMAT
                    )
                )
                .registerTypeAdapter(
                    TimeZone::class.java,
                    TimezoneDeserializer()
                )
                .setFieldNamingStrategy { field ->
                    FieldNamingPolicy
                        .LOWER_CASE_WITH_UNDERSCORES
                        .translateName(field)
                }
                .create()
        )
    }
}