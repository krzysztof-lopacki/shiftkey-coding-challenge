package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.availableshiftslist

import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailableShiftsListFragment : Fragment(R.layout.fragment_available_shifts_list) {
    val viewModel: AvailableShiftsListViewModel by hiltNavGraphViewModels(R.id.available_shifts_list)

}