package test.febri.spaceflightnews.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import test.febri.githubapp.util.NavConstant
import test.febri.spaceflightnews.databinding.FragmentHomeBinding
import test.febri.spaceflightnews.home.adapter.HomeNewsListAdapter


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var articleAdapter: HomeNewsListAdapter
    private lateinit var blogAdapter: HomeNewsListAdapter
    private lateinit var reportAdapter: HomeNewsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupListeners()
    }

    private fun setupRecyclerView() {
        articleAdapter = HomeNewsListAdapter()
        binding.rvArticles.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = articleAdapter
        }
        blogAdapter = HomeNewsListAdapter()
        binding.rvBlogs.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = blogAdapter
        }
    }

    private fun setupListeners() {
        binding.tvLabelArticleSeeMore.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchNewsFragment(NavConstant.NAV_ARGS_SEARCH_TYPE_ARTICLE)
            )
        }
        binding.tvLabelBlogSeeMore.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchNewsFragment(NavConstant.NAV_ARGS_SEARCH_TYPE_BLOG)
            )
        }
        binding.tvLabelReportSeeMore.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchNewsFragment(NavConstant.NAV_ARGS_SEARCH_TYPE_REPORT)
            )
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.articles.collect { articleList ->
                        articleAdapter.submitList(articleList)
                    }
                }
                launch {
                    viewModel.blogs.collect { blogList ->
                        blogAdapter.submitList(blogList)
                    }
                }
            }
        }
    }
}
