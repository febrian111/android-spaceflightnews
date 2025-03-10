package test.febri.spaceflightnews.searchnews.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import test.febri.domain.util.AppConst
import test.febri.spaceflightnews.databinding.DialogSortFilterBinding
import test.febri.spaceflightnews.searchnews.SearchNewsViewModel
import test.febri.spaceflightnews.searchnews.adapter.SortFilterAdapter

class SortFilterBottomListDialogFragment(private val viewModel: SearchNewsViewModel) : BottomSheetDialogFragment() {

    companion object {
        const val TAG_SORT = "sort"
        const val TAG_FILTER = "filter"
    }

    private lateinit var binding: DialogSortFilterBinding

    private var sortFilterAdapter: SortFilterAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSortFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (tag == TAG_SORT) {
            setupSortAdapter()
        } else if (tag == TAG_FILTER) {
            setupFilterAdapter()
        }
    }

    private fun setupSortAdapter() {
        sortFilterAdapter = SortFilterAdapter {
            viewModel.setSortOrder(it)
            dismiss()
        }
        binding.rvSortFilter.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = sortFilterAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sortOptions.collect {
                sortFilterAdapter?.submitList(it)
            }
        }
    }

    private fun setupFilterAdapter() { //todo setup for news site filter

    }
}