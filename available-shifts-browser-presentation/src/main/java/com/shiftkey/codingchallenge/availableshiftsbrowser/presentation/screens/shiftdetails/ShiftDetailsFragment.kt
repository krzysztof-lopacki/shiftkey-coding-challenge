package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.shiftdetails

import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShiftDetailsFragment : Fragment(R.layout.fragment_shift_details) {
    val viewModel: ShiftDetailsViewModel by hiltNavGraphViewModels(R.id.shift_details)
}