package com.shiftkey.codingchallenge.availableshiftsbrowser.repository

import androidx.paging.PagingSource
import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Day
import io.reactivex.rxjava3.core.Completable
import java.util.*

interface AvailableShiftsRepository {
    fun getAvailableShifts(): PagingSource<Calendar, Day>
}