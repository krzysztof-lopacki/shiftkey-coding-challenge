package com.shiftkey.codingchallenge.availableshiftsbrowser.repository

import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Hint
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface HintsRepository {
    fun markHintAsRead(hintId: Int): Completable
    fun getHints(): Single<List<Hint>>
}