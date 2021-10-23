package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.shiftdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.R
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.databinding.FragmentShiftDetailsBinding
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.models.Shift
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShiftDetailsFragment : Fragment(R.layout.fragment_shift_details) {
    @Inject internal lateinit var viewModelFactory: ShiftDetailsViewModel.Factory
    private val args: ShiftDetailsFragmentArgs by navArgs()
    private val viewModel: ShiftDetailsViewModel by viewModels { ShiftDetailsViewModelFactory(viewModelFactory, args.shift) }
    private lateinit var binding: FragmentShiftDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        observeState()
    }

    private fun setupView(view: View) {
        binding = FragmentShiftDetailsBinding.bind(view)
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) { state ->
        binding.shift = state.shift
    }
}

class ShiftDetailsViewModelFactory(
    private val shiftDetailsViewModelFactory: ShiftDetailsViewModel.Factory,
    private val shift: Shift
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return shiftDetailsViewModelFactory.create(shift) as T
    }
}
