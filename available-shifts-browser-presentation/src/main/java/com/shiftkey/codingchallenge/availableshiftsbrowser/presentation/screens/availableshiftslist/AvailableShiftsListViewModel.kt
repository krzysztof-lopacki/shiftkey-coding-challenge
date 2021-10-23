package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.availableshiftslist

import androidx.lifecycle.*
import androidx.paging.*
import com.shiftkey.codingchallenge.availableshiftsbrowser.config.Configuration
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.models.Shift
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.models.ShiftsListItem
import com.shiftkey.codingchallenge.availableshiftsbrowser.usecases.GetAvailableShiftsUseCase
import com.shiftkey.codingchallenge.availableshiftsbrowser.usecases.GetUnreadHintsCountUseCase
import com.shiftkey.codingchallenge.livedata.HandleableEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

private const val STATE_HINTS_SHOWN_KEY = "hints_shown_key"

@ExperimentalPagingApi
@HiltViewModel
class AvailableShiftsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    configuration: Configuration,
    getUnreadHintsCountUseCase: GetUnreadHintsCountUseCase,
    getAvailableShiftsUseCase: GetAvailableShiftsUseCase
) : ViewModel() {
    private val subscriptions = CompositeDisposable()
    private val _viewEffect = MutableLiveData<HandleableEvent<ViewEffect>>()
    val viewEffect: LiveData<HandleableEvent<ViewEffect>> = _viewEffect
    private val _navigationEffect = MutableLiveData<HandleableEvent<NavigationEffect>>()
    val navigationEffect: LiveData<HandleableEvent<NavigationEffect>> = _navigationEffect
    private val _state = MutableLiveData(State(
        isLoading = true,
        shifts = MutableLiveData(null)))
    val state: LiveData<State> = _state
    private val showingHintAllowed = savedStateHandle.getLiveData(STATE_HINTS_SHOWN_KEY, true)

    init {
        val shifts = Pager(
            PagingConfig(1, 1, false, 1),
            Calendar.getInstance().apply { timeZone = configuration.searchAreaTimeZone },
            null,
            getAvailableShiftsUseCase::execute
        ).flow
            .map { pagingData ->
                pagingData.flatMap { day ->
                    val items = mutableListOf<ShiftsListItem>()
                    items.add(ShiftsListItem.DaysSeparatorItem.valueOf(day))
                    items.addAll(day.shifts.map { shift -> ShiftsListItem.ShiftItem.valueOf(shift) })
                    items
                }
            }
            .cachedIn(viewModelScope)
            .asLiveData()

        _state.value = _state.value?.copy(
            isLoading = false,
            shifts = shifts
        )

        if (showingHintAllowed.value == true) {
            showingHintAllowed.value = false
            subscriptions.add(
                getUnreadHintsCountUseCase.execute().subscribe { count ->
                    if (count > 0) {
                        _navigationEffect.value = HandleableEvent(NavigationEffect.ShowHints)
                    }
                }
            )
        }
    }

    fun onEvent(event: ViewEvent) {
        when (event) {
            ViewEvent.OnRetryClicked -> _viewEffect.value = HandleableEvent(ViewEffect.RetryDataLoading)
            ViewEvent.OnRefreshRequested -> _viewEffect.value = HandleableEvent(ViewEffect.InvalidateShiftsList)
            is ViewEvent.OnShiftClicked -> _navigationEffect.value = HandleableEvent(NavigationEffect.ShowShiftDetails(event.shift))
        }
    }

    data class State (
        val isLoading: Boolean,
        val shifts: LiveData<PagingData<ShiftsListItem>>
    )

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    sealed class ViewEffect {
        object RetryDataLoading : ViewEffect()
        object InvalidateShiftsList : ViewEffect()
    }

    sealed class NavigationEffect {
        data class ShowShiftDetails(val shift: Shift) : NavigationEffect()
        object ShowHints : NavigationEffect()
    }

    sealed class ViewEvent {
        object OnRefreshRequested : ViewEvent()
        object OnRetryClicked : ViewEvent()
        data class OnShiftClicked(val shift: Shift) : ViewEvent()
    }
}