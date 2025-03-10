package test.febri.spaceflightnews.searchnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import test.febri.githubapp.util.NavConstant
import test.febri.spaceflightnews.databinding.FragmentSearchNewsBinding
import test.febri.spaceflightnews.searchnews.adapter.NewsPagingAdapter
import test.febri.spaceflightnews.searchnews.dialog.SortFilterBottomListDialogFragment

@AndroidEntryPoint
class SearchNewsFragment : Fragment() {

    companion object {
        const val SORT = "sort"
        const val FILTER = "filter"
    }

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchNewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchBar()
        setupFilterAndSortButtons()
        observeNews()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsPagingAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val searchQuery = searchView.text.toString()
                searchBar.setText(searchQuery)
                searchView.hide()
                viewModel.setSearchQuery(searchQuery)
                true
            }
        }
    }

    private fun setupFilterAndSortButtons() {
        binding.bottomFilterButton.setOnClickListener {
            // Implement filter logic (e.g., show a dialog to select categories)
        }

        binding.bottomSortButton.setOnClickListener {
            // Implement sort logic (e.g., toggle between newest and oldest)
            SortFilterBottomListDialogFragment(viewModel).show(childFragmentManager, SORT)
        }
    }

    private fun observeNews() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.searchType.collect { searchType ->
                        (requireActivity() as AppCompatActivity).supportActionBar?.title = when (searchType) {
                            NavConstant.NAV_ARGS_SEARCH_TYPE_ARTICLE -> "Articles"
                            NavConstant.NAV_ARGS_SEARCH_TYPE_BLOG -> "Blogs"
                            NavConstant.NAV_ARGS_SEARCH_TYPE_REPORT -> "Reports"
                            else -> "News List"
                        }
                    }
                }

                // Collect news or articles
                launch {
                    viewModel.news.collect { pagingData ->
                        newsAdapter.submitData(pagingData)
                    }
                }

                // Collect refresh state
//                launch {
//                    viewModel.refreshing.collect { isRefreshing ->
//                        binding.swipeRefresh.isRefreshing = isRefreshing
//                    }
//                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}