package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.hint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Hint
import com.shiftkey.codingchallenge.availableshiftsbrowser.usecases.GetUnreadHintsUseCase
import com.shiftkey.codingchallenge.availableshiftsbrowser.usecases.MarkHintAsReadUseCase
import com.shiftkey.codingchallenge.livedata.HandleableEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HintsListViewModel @Inject constructor(
    private val markHintAsReadUseCase: MarkHintAsReadUseCase,
    private val getUnreadHintsUseCase: GetUnreadHintsUseCase
) : ViewModel() {
    private val subscriptions = CompositeDisposable()
    private val _state = MutableLiveData<State>(null)
    val state: LiveData<State> = _state
    private val _navigationEffect = MutableLiveData<HandleableEvent<NavigationEffect>>()
    val navigationEffect: LiveData<HandleableEvent<NavigationEffect>> = _navigationEffect
    private lateinit var hints: MutableList<Hint>

    init {
        loadHints()
    }

    fun onEvent(viewEffect: ViewEffect) {
        when (viewEffect) {
            ViewEffect.OnCloseClicked -> _navigationEffect.value =
                HandleableEvent(NavigationEffect.NavigateBack)
            ViewEffect.OnNextClicked -> showNextHint()
        }
    }

    private fun loadHints() {
        subscriptions.add(getUnreadHintsUseCase
            .execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoadingIndicator()
            }
            .subscribe { hints ->
                hideLoadingIndicator()

                this.hints = mutableListOf<Hint>().apply { addAll(hints) }
                showFirstHintOrExit()
            })
    }

    private fun showLoadingIndicator() {
        _state.value = State(
            isLoading = true,
            closeButtonVisible = false,
            nextButtonVisible = false,
            hintMessage = ""
        )
    }

    private fun hideLoadingIndicator() {
        _state.value = _state.value?.copy(
            isLoading = false
        )
    }

    private fun showFirstHintOrExit() {
        if (this.hints.size == 0) {
            _navigationEffect.value = HandleableEvent(NavigationEffect.NavigateBack)
        } else {
            showHint()
        }
    }

    private fun showNextHint() {
        hints.removeFirst()
        showFirstHintOrExit()
    }

    private fun showHint() {
        val hint = hints.first()
        _state.value = _state.value?.copy(
            closeButtonVisible = true,
            nextButtonVisible = hints.size > 1,
            hintMessage = hint.message
        )

        subscriptions.add(
            markHintAsReadUseCase
                .execute(hint.id)
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    sealed class ViewEffect {
        object OnCloseClicked : ViewEffect()
        object OnNextClicked : ViewEffect()
    }

    sealed class NavigationEffect {
        object NavigateBack : NavigationEffect()
    }

    data class State(
        val isLoading: Boolean,
        val closeButtonVisible: Boolean,
        val nextButtonVisible: Boolean,
        val hintMessage: String
    )
}