package com.shiftkey.codingchallenge.availableshiftsbrowser.usecases

import com.shiftkey.codingchallenge.availableshiftsbrowser.repository.HintsRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class MarkHintAsReadUseCase @Inject constructor(
    private val hintsRepository: HintsRepository
) {
    fun execute(hintId: Int): Completable = hintsRepository.markHintAsRead(hintId)
}