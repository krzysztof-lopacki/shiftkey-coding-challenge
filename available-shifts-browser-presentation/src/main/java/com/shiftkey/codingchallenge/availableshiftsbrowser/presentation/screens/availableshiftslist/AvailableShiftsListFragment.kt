package com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.availableshiftslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.findNavController
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.R
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.databinding.AvailableShiftsListDaysDividerBinding
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.databinding.AvailableShiftsListFooterBinding
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.databinding.AvailableShiftsListItemBinding
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.databinding.FragmentAvailableShiftsListBinding
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.models.Shift
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.models.ShiftsListItem
import com.shiftkey.codingchallenge.availableshiftsbrowser.presentation.screens.availableshiftslist.AvailableShiftsListViewModel.*
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagingApi
@AndroidEntryPoint
class AvailableShiftsListFragment : Fragment(R.layout.fragment_available_shifts_list) {
    private val viewModel: AvailableShiftsListViewModel by hiltNavGraphViewModels(R.id.available_shifts_list)
    private lateinit var binding: FragmentAvailableShiftsListBinding
    private lateinit var shiftsAdapter: ShiftsAdapter
    private lateinit var shiftsLoadingStateObserver: (CombinedLoadStates) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        observeState()
        observeViewEffects()
        observeNavigationEffects()
    }

    private fun setupView(view: View) {
        binding = FragmentAvailableShiftsListBinding.bind(view)

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.onEvent(ViewEvent.OnRefreshRequested)
        }

        binding.retryButton.setOnClickListener {
            viewModel.onEvent(ViewEvent.OnRetryClicked)
        }

        shiftsAdapter = ShiftsAdapter { shift ->
            viewModel.onEvent(ViewEvent.OnShiftClicked(shift))
        }

        binding.shiftsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.shiftsList.itemAnimator = null
        binding.shiftsList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.shiftsList.adapter = shiftsAdapter
            .withLoadStateFooter(FooterAdapter {
                viewModel.onEvent(ViewEvent.OnRetryClicked)
            })

        shiftsLoadingStateObserver = { combinedLoadStates ->
            if (shiftsAdapter.itemCount > 0) {
                binding.swipeToRefresh.isEnabled = true
                binding.swipeToRefresh.isRefreshing = combinedLoadStates.refresh is LoadState.Loading
                binding.loadingIndicator.isVisible = false
                if (combinedLoadStates.refresh is LoadState.Error) {
                    Toast.makeText(requireContext(),
                        R.string.available_shifts_list_refresh_failed, Toast.LENGTH_LONG).show()
                }
            } else {
                binding.swipeToRefresh.isEnabled = false
                binding.loadingIndicator.isVisible = combinedLoadStates.refresh is LoadState.Loading
                binding.retryButton.isVisible = combinedLoadStates.refresh is LoadState.Error
            }
        }
    }

    private fun observeState() {
        // observe shifts loading
        viewModel.state
            .switchMap { state -> state.shifts }
            .observe(viewLifecycleOwner) { pagingData ->
                if (pagingData != null) {
                    /* #REVIEW: In fact in ideal-world scenario we should not register
                     * any state listeners on an adapter - the view should be solely driven
                     * by the State dispatched by the view model.
                     * Unfortunately Android Paging 3 library does not work well with MVI style
                     * and Clean Architecture in general.
                     */
                    shiftsAdapter.addLoadStateListener(shiftsLoadingStateObserver)
                    shiftsAdapter.submitData(lifecycle, pagingData)
                } else {
                    shiftsAdapter.removeLoadStateListener(shiftsLoadingStateObserver)
                    shiftsAdapter.submitData(lifecycle, PagingData.empty())
                }
            }
    }

    private fun observeViewEffects() {
        viewModel.viewEffect.observe(viewLifecycleOwner) { event ->
            event.handle { viewEffect ->
                when(viewEffect) {
                    ViewEffect.RetryDataLoading -> shiftsAdapter.retry()
                    ViewEffect.InvalidateShiftsList -> shiftsAdapter.refresh()
                }
                true
            }
        }
    }

    private fun observeNavigationEffects() {
        viewModel.navigationEffect.observe(viewLifecycleOwner) { event ->
            event.handle { navigationEffect ->
                when(navigationEffect) {
                    is NavigationEffect.ShowShiftDetails -> {
                        findNavController().navigate(AvailableShiftsListFragmentDirections.toShiftDetails(navigationEffect.shift))
                    }
                    NavigationEffect.ShowHints -> {
                        findNavController().navigate(AvailableShiftsListFragmentDirections.toHintsList())
                    }
                }
                true
            }
        }
    }
}

private class ShiftsAdapter(
    private val shiftClickedListener: (Shift) -> Unit
) : PagingDataAdapter<ShiftsListItem, RecyclerView.ViewHolder>(this) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ShiftViewHolder) holder.bind(getItem(position) as ShiftsListItem.ShiftItem)
        else if (holder is DaysDividerViewHolder) holder.bind(getItem(position) as ShiftsListItem.DaysSeparatorItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_DAY_DIVIDER) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.available_shifts_list_days_divider, parent, false)
            DaysDividerViewHolder(view)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.available_shifts_list_item, parent, false)
            ShiftViewHolder(view, shiftClickedListener)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is ShiftsListItem.ShiftItem) ITEM_TYPE_SHIFT else ITEM_TYPE_DAY_DIVIDER
    }

    companion object : DiffUtil.ItemCallback<ShiftsListItem>() {
        private const val ITEM_TYPE_DAY_DIVIDER = 0
        private const val ITEM_TYPE_SHIFT = 1

        override fun areItemsTheSame(shift1: ShiftsListItem, shift2: ShiftsListItem): Boolean {
            return shift1 is ShiftsListItem.ShiftItem && shift2 is ShiftsListItem.ShiftItem
                    && shift1.shift.shiftId == shift2.shift.shiftId
                    || shift1 == shift2
        }

        override fun areContentsTheSame(shift1: ShiftsListItem, shift2: ShiftsListItem): Boolean {
            return shift1 == shift2
        }
    }
}

private class FooterAdapter(
    private val retryButtonClickListener: (view: View) -> Unit
) : LoadStateAdapter<FooterViewHolder>() {
    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.available_shifts_list_footer, parent, false)
        return FooterViewHolder(view, retryButtonClickListener)
    }
}

private class ShiftViewHolder(
    view: View,
    private val onClickedListener: (Shift) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = AvailableShiftsListItemBinding.bind(view)

    init {
        binding.root.setOnClickListener {
            onClickedListener(binding.shift!!)
        }
    }

    fun bind(shiftItem: ShiftsListItem.ShiftItem) {
        binding.shift = shiftItem.shift
        binding.executePendingBindings()
    }
}

private class DaysDividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = AvailableShiftsListDaysDividerBinding.bind(view)

    fun bind(day: ShiftsListItem.DaysSeparatorItem) {
        binding.message.text = day.date
    }
}

private class FooterViewHolder(
    view: View,
    retryButtonClickListener: (view: View) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = AvailableShiftsListFooterBinding.bind(view)

    init {
        binding.retryButton.setOnClickListener(retryButtonClickListener)
    }

    fun bind(loadState: LoadState) {
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.loadingIndicator.isVisible = loadState is LoadState.Loading
        binding.root.isVisible = loadState !is LoadState.NotLoading
    }
}