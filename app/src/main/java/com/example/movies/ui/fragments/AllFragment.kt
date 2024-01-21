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
import com.example.movies.ui.adapters.AllFragmentAdapter
import com.example.movies.ui.adapters.ProgressBarAdapter
import com.example.movies.utils.MovieUtils.checkNetwork
import com.example.movies.viewmodels.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
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
        callObservers()
    }

    private fun callObservers() {
        /* viewModel.movieListMutableLiveData.observe(viewLifecycleOwner) {
             Timber.e("it: ${it.toString()}")

             if (!it.isNullOrEmpty()) {
                 Timber.e("size: ${it.size}")
                 adapter.submitList(it)
             }
         }*/

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
        viewModel.callMoviesListApi(checkNetwork(requireContext()))
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
                                    "No Data To Show",
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
                    return if (position == adapter.itemCount && progressBarAdapter.itemCount > 0) {
                        2
                    } else {
                        1
                    }
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
        viewModel.callGenreListApi(checkNetwork(requireContext()))
        viewModel.callMoviesListApi(checkNetwork(requireContext()))
    }


}