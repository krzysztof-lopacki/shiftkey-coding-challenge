package com.shiftkey.codingchallenge.availableshiftsbrowser.usecases

import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Hint
import com.shiftkey.codingchallenge.availableshiftsbrowser.repository.HintsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetUnreadHintsUseCase @Inject constructor(
    private val hintsRepository: HintsRepository
) {
    fun execute(): Single<List<Hint>> = hintsRepository
        .getHints()
        .map { hints ->
            hints.filter { hint -> !hint.read }
        }
}