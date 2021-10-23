package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.shiftdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.models.Shift
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * #REVIEW: Assisted injection is obsolete in this implementation of the view model
 * as we don't inject anything else. I just wanted to show how usually the parameters are passed
 * to the view model.
 */
class ShiftDetailsViewModel @AssistedInject constructor(
    @Assisted shift: Shift,
) : ViewModel() {
    private val _state = MutableLiveData(State(true, null))
    val state: LiveData<State> = _state

    init {
        _state.value = _state.value?.copy(isLoading = false, shift = shift)
    }

    @AssistedFactory
    interface Factory {
        fun create(shift: Shift): ShiftDetailsViewModel
    }

    data class State (
        val isLoading: Boolean,
        val shift: Shift? = null
    )
}