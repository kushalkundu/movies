package com.example.movies.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.movies.MainActivity
import com.example.movies.R
import com.example.movies.databinding.FragmentAllBinding
import com.example.movies.models.Genre
import com.example.movies.ui.adapters.AllFragmentAdapter
import com.example.movies.ui.adapters.ProgressBarAdapter
import com.example.movies.utils.MovieUtils.checkNetwork
import com.example.movies.viewmodels.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


// Fragment for all Genre Tabs
@AndroidEntryPoint(Fragment::class)
class AllFragment : Hilt_AllFragment(), OnRefreshListener {


    private lateinit var binding: FragmentAllBinding
    private lateinit var adapter: AllFragmentAdapter
    private lateinit var viewModel: MoviesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as MainActivity).viewModel
        initUI()
        handleTabLayout()
        callObservers()
    }

    private fun callObservers() {
        viewModel.apply {
            genreMutableLiveData.observe(requireActivity()) {
                if (!it.isNullOrEmpty()) {
                    it.forEach { genre -> createTabItem(genre) }
                }
            }

            isLoadingProgressBar.observe(requireActivity()) {
                if (checkNetwork(requireContext())) {
                    binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.let {
                        if (it != null) {
                            binding.srlAll.isRefreshing = false
                            adapter.submitData(lifecycle, it)
                        }
                    }
                }
            }
        }
    }

    private fun initUI() {
        binding.srlAll.setOnRefreshListener(this)
        createTabItem(Genre(id = 0, name = resources.getString(R.string.all)))
        viewModel.callMoviesListApi(checkNetwork(requireContext()))
        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        binding.apply {
            adapter = AllFragmentAdapter(requireContext(), onClick = { movieId ->
                viewModel.callMovieDetails(
                    movieId,
                    checkNetwork(requireContext()),
                    callFunction = {
                        requireActivity().runOnUiThread {
                            if (it) {
                                if (findNavController().currentDestination?.id == R.id.allFragment) {
                                    findNavController().navigate(R.id.detailFragment)
                                }
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    requireContext().resources.getString(R.string.no_data_to_show),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                    })
            })
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            val progressBarAdapter = ProgressBarAdapter()
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == adapter.itemCount && progressBarAdapter.itemCount > 0) 2 else 1
                }
            }
            recyclerAll.layoutManager = gridLayoutManager
            recyclerAll.adapter = adapter
            recyclerAll.adapter = adapter.withLoadStateFooter(
                progressBarAdapter
            )
        }
    }

    override fun onRefresh() {
        viewModel.apply {
            callGenreListApi(checkNetwork(requireContext()))
            callMoviesListApi(checkNetwork(requireContext()))
        }
    }

    private fun handleTabLayout() {

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.genreSelected = tab!!.id
                viewModel.callMoviesListApi(checkNetwork(requireContext()))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do Nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do Nothing
            }

        })
    }


    private fun createTabItem(genre: Genre) {
        val tab = binding.tabLayout.newTab().apply {
            this.text = genre.name
            this.id = genre.id!!
        }
        binding.tabLayout.addTab(tab)
    }

}