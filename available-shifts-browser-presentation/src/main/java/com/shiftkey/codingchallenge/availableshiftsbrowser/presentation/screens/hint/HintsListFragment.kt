package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.hint

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.R
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.databinding.FragmentHintsPagerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HintsListFragment : DialogFragment(R.layout.fragment_hints_pager) {
    private val viewModel: HintsListViewModel by hiltNavGraphViewModels(R.id.hints_pager)
    private lateinit var binding: FragmentHintsPagerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        observeState()
        observeNavigationEffect()
    }

    private fun setupView(view: View) {
        binding = FragmentHintsPagerBinding.bind(view)
        binding.closeButton.setOnClickListener {
            viewModel.onEvent(HintsListViewModel.ViewEffect.OnCloseClicked)
        }
        binding.nextButton.setOnClickListener {
            viewModel.onEvent(HintsListViewModel.ViewEffect.OnNextClicked)
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.nextButton.isVisible = state.nextButtonVisible
            binding.closeButton.isVisible = state.closeButtonVisible
            binding.message.isVisible = state.hintMessage.isEmpty().not()
            binding.message.text = state.hintMessage
            binding.loadingIndicator.isVisible = state.isLoading
        }
    }

    private fun observeNavigationEffect() {
        viewModel.navigationEffect.observe(viewLifecycleOwner) { event ->
            event.handle { navigationEffect ->
                when (navigationEffect) {
                    is HintsListViewModel.NavigationEffect.NavigateBack-> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}