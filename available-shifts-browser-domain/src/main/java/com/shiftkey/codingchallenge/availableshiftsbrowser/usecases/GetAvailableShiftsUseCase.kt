package com.shiftkey.codingchallenge.availableshiftsbrowser.usecases

import com.shiftkey.codingchallenge.availableshiftsbrowser.repository.AvailableShiftsRepository
import javax.inject.Inject

class GetAvailableShiftsUseCase @Inject constructor(
    private val availableShiftsRepository: AvailableShiftsRepository
) {
    fun execute() = availableShiftsRepository.getAvailableShifts()
}