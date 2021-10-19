package com.shiftkey.codingchallenge.config

import android.content.Context
import com.shiftkey.codingchallenge.R
import com.shiftkey.codingchallenge.availableshiftsbrowser.config.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*

class AvailableShiftsBrowserConfiguration @Inject constructor(
    @ApplicationContext application: Context
) : Configuration {
    override val shiftsApiUrl: String = application.getString(R.string.configuration_shifts_api_url)
    override val searchArea: String = application.getString(R.string.configuration_search_area)
    override val searchAreaTimeZone: TimeZone = TimeZone.getTimeZone(application.getString(R.string.configuration_search_area_timezone))
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AvailableShiftsBrowserConfigurationModule {
    @Binds
    abstract fun bindConfiguration(config: AvailableShiftsBrowserConfiguration): Configuration
}