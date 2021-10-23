package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.hint

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Hint
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.hint.HintsListViewModel.NavigationEffect
import com.shiftkey.codingchallenge.availableshiftsbrowser.usecases.GetUnreadHintsUseCase
import com.shiftkey.codingchallenge.availableshiftsbrowser.usecases.MarkHintAsReadUseCase
import com.shiftkey.codingchallenge.livedata.HandleableEvent
import com.shiftkey.codingchallenge.test.RxAndroidSchedulerRule
import com.shiftkey.codingchallenge.test.RxJavaSchedulerRule
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

private val HINT_1 = Hint(1, "hint1", false)
private val HINT_2 = Hint(2, "hint2", false)
private val HINT_3 = Hint(3, "hint3", false)
private val HINTS_LIST = listOf(HINT_1, HINT_2, HINT_3)
private val EMPTY_HINTS_LIST = emptyList<Hint>()

class HintsListViewModelTest {
    @get:Rule
    val rxAndroidSchedulerRule = RxAndroidSchedulerRule()

    @get:Rule
    val rxJavaSchedulerRule = RxJavaSchedulerRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var markHintAsReadUseCase: MarkHintAsReadUseCase

    @Mock
    lateinit var getUnreadHintsUseCase: GetUnreadHintsUseCase

    lateinit var viewModel: HintsListViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `given provided hints list is empty when screen is loaded then it closes automatically`() {
        given(getUnreadHintsUseCase.execute()).willReturn(Single.just(EMPTY_HINTS_LIST))
        viewModel = HintsListViewModel(markHintAsReadUseCase, getUnreadHintsUseCase)

        assertEquals(
            HandleableEvent(NavigationEffect.NavigateBack),
            viewModel.navigationEffect.value
        )
    }

    @Test
    fun `given provided hints list is not empty when screen is loaded the first hint is shown`() {
        given(getUnreadHintsUseCase.execute()).willReturn(Single.just(HINTS_LIST))
        given(markHintAsReadUseCase.execute(anyInt())).willReturn(Completable.complete())

        viewModel = HintsListViewModel(markHintAsReadUseCase, getUnreadHintsUseCase)

        assertEquals(
            HintsListViewModel.State(
                isLoading = false,
                closeButtonVisible = true,
                nextButtonVisible = true,
                HINTS_LIST.first().message
            ),
            viewModel.state.value
        )
    }

    @Test
    fun `given provided hints list is not empty when hint is shown it is marked as shown`() {
        given(getUnreadHintsUseCase.execute()).willReturn(Single.just(HINTS_LIST))
        given(markHintAsReadUseCase.execute(anyInt())).willReturn(Completable.complete())

        viewModel = HintsListViewModel(markHintAsReadUseCase, getUnreadHintsUseCase)

        verify(markHintAsReadUseCase).execute(HINT_1.id)
    }

    @Test
    fun `given provided hints list is not empty and a hint is shown when user clicks next button then next hint is shown`() {
        given(getUnreadHintsUseCase.execute()).willReturn(Single.just(HINTS_LIST))
        given(markHintAsReadUseCase.execute(anyInt())).willReturn(Completable.complete())

        viewModel = HintsListViewModel(markHintAsReadUseCase, getUnreadHintsUseCase)
        viewModel.onEvent(HintsListViewModel.ViewEffect.OnNextClicked)

        assertEquals(
            HintsListViewModel.State(
                isLoading = false,
                closeButtonVisible = true,
                nextButtonVisible = true,
                HINTS_LIST[1].message
            ),
            viewModel.state.value
        )
    }

    @Test
    fun `given provided hints list is not empty when the last hint is shown then next button is not visible`() {
        given(getUnreadHintsUseCase.execute()).willReturn(Single.just(HINTS_LIST))
        given(markHintAsReadUseCase.execute(anyInt())).willReturn(Completable.complete())

        viewModel = HintsListViewModel(markHintAsReadUseCase, getUnreadHintsUseCase)
        viewModel.onEvent(HintsListViewModel.ViewEffect.OnNextClicked)
        viewModel.onEvent(HintsListViewModel.ViewEffect.OnNextClicked)

        assertEquals(
            HintsListViewModel.State(
                isLoading = false,
                closeButtonVisible = true,
                nextButtonVisible = false,
                HINTS_LIST[2].message
            ),
            viewModel.state.value
        )
    }

    @Test
    fun `given provided hints list is loading when screen is shown then the loading indicator is visible and no buttons are visible`() {
        given(getUnreadHintsUseCase.execute()).willReturn(Single.never())

        viewModel = HintsListViewModel(markHintAsReadUseCase, getUnreadHintsUseCase)

        assertEquals(
            HintsListViewModel.State(
                isLoading = true,
                closeButtonVisible = false,
                nextButtonVisible = false,
                ""
            ),
            viewModel.state.value
        )
    }

    @Test
    fun `given provided hints list is not empty and a hint is shown when user clicks close the screen closes`() {
        given(getUnreadHintsUseCase.execute()).willReturn(Single.just(HINTS_LIST))
        given(markHintAsReadUseCase.execute(anyInt())).willReturn(Completable.complete())

        viewModel = HintsListViewModel(markHintAsReadUseCase, getUnreadHintsUseCase)
        viewModel.onEvent(HintsListViewModel.ViewEffect.OnCloseClicked)

        assertEquals(
            HandleableEvent(NavigationEffect.NavigateBack),
            viewModel.navigationEffect.value
        )
    }

}