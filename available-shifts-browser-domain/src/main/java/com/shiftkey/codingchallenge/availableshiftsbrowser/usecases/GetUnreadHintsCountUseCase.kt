package com.shiftkey.codingchallenge.availableshiftsbrowser.usecases

import com.shiftkey.codingchallenge.availableshiftsbrowser.repository.HintsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetUnreadHintsCountUseCase @Inject constructor(
    private val hintsRepository: HintsRepository
) {
    fun execute(): Single<Int> = hintsRepository
        .getHints()
        .map { hints -> hints.count { hint -> !hint.read } }

}