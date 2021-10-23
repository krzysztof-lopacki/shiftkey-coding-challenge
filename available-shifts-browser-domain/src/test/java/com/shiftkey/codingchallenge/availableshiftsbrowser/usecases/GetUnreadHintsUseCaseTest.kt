package com.shiftkey.codingchallenge.availableshiftsbrowser.usecases

import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Hint
import com.shiftkey.codingchallenge.availableshiftsbrowser.repository.HintsRepository
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations

private val HINT_1 = Hint(1, "message1", true)
private val HINT_2 = Hint(2, "message2", false)
private val HINT_3 = Hint(3, "message3", true)
private val ALL_HINTS = listOf(HINT_1, HINT_2, HINT_3)

class GetUnreadHintsUseCaseTest {
    @Mock
    lateinit var hintsRepository: HintsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `given some hint are read already when getHints is called return only unread hints`() {
        given(hintsRepository.getHints()).willReturn(Single.just(ALL_HINTS))

        val useCase = GetUnreadHintsUseCase(hintsRepository)
        val testObserver = useCase.execute().test()

        val expectedHints = listOf(HINT_2)
        testObserver.assertValue(expectedHints)
    }

    @Test
    fun `given no hints are present in the repo when getHints is called return empty list of hints`() {
        given(hintsRepository.getHints()).willReturn(Single.just(emptyList()))

        val useCase = GetUnreadHintsUseCase(hintsRepository)
        val testObserver = useCase.execute().test()

        val expectedHints = emptyList<Hint>()
        testObserver.assertValue(expectedHints)
    }
}