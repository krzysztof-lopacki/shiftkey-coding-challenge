package com.shiftkey.codingchallenge.availableshiftsbrowser.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.shiftkey.codingchallenge.availableshiftsbrowser.config.Configuration
import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Day
import com.shiftkey.codingchallenge.availableshiftsbrowser.remote.shifts.AvailableShiftsFetcher
import com.shiftkey.codingchallenge.calendar.getFirstDayOfTheNextWeek
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitBasedAvailableShiftsRepository @Inject constructor(
    private val configuration: Configuration,
    private val availableShiftsFetcher: AvailableShiftsFetcher
) : AvailableShiftsRepository {
    override fun getAvailableShifts(): PagingSource<Calendar, Day> =
        object : RxPagingSource<Calendar, Day>() {
            override fun loadSingle(params: LoadParams<Calendar>): Single<LoadResult<Calendar, Day>> {
                return availableShiftsFetcher
                    .getAvailableShiftsForAWeek(
                        params.key!!,
                        configuration.searchArea
                    )
                    .map<LoadResult<Calendar, Day>> { days ->
                        val nextWeek = params.key?.getFirstDayOfTheNextWeek()
                        LoadResult.Page(days, null, nextWeek)
                    }
                    .onErrorResumeNext { error ->
                        Single.just(LoadResult.Error(error))
                    }
            }

            override fun getRefreshKey(state: PagingState<Calendar, Day>): Calendar? = null
        }
}