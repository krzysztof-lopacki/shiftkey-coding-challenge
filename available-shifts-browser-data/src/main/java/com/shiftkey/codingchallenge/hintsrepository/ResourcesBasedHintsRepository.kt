package com.shiftkey.codingchallenge.hintsrepository

import android.content.Context
import android.content.SharedPreferences
import com.shiftkey.codingchallenge.availableshiftsbrowser.data.R
import com.shiftkey.codingchallenge.availableshiftsbrowser.models.Hint
import com.shiftkey.codingchallenge.availableshiftsbrowser.repository.HintsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ResourcesBasedHintsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : HintsRepository {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.hints_repository_shared_preferences),
        Context.MODE_PRIVATE
    )
    private var hintMessagesById = mapOf(
        R.string.hint_repository_hint_1 to context.getString(R.string.hint_repository_hint_1),
        R.string.hint_repository_hint_2 to context.getString(R.string.hint_repository_hint_2)
    )

    override fun markHintAsRead(hintId: Int): Completable = Completable.fromAction {
        val hintKey = context.getString(R.string.hints_repository_hint_read_key, hintId)
        sharedPreferences.edit().putBoolean(hintKey, true).commit()
    }

    private fun isHintRead(hintId: Int): Boolean {
        val hintKey = context.getString(R.string.hints_repository_hint_read_key, hintId)
        return sharedPreferences.getBoolean(hintKey, false)
    }

    override fun getHints(): Single<List<Hint>> = Single.fromCallable {
        hintMessagesById.entries
            .map { entry ->
                Hint (
                    id = entry.key,
                    message = entry.value,
                    isHintRead(entry.key)
                )
            }
    }
}