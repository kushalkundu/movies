package com.example.movies.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.example.movies.MainActivity
import com.example.movies.R
import com.example.movies.databinding.FragmentDetailBinding
import com.example.movies.models.GenreX
import com.example.movies.models.MovieDetailModel
import com.example.movies.network.URL
import com.example.movies.utils.Constants.DD_MM_YYYY
import com.example.movies.utils.Constants.YYYY_MM_DD
import com.example.movies.utils.MovieUtils.checkNetwork
import com.example.movies.utils.MovieUtils.getFormattedDate
import com.example.movies.viewmodels.MoviesViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

// Fragment for Movie Details
@AndroidEntryPoint(Fragment::class)
class DetailFragment : Hilt_DetailFragment(), OnRefreshListener {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: MoviesViewModel
    private var movieId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as MainActivity).viewModel
        initUi()
        callObservers()
    }

    private fun callObservers() {
        viewModel.movieDetailMutableLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.srlDetail.isRefreshing = false
                setData(it)
            }
        }
    }

    private fun setData(movieDetailModel: MovieDetailModel) {
        movieId = movieDetailModel.id
        Glide.with(requireContext()).load("${URL.IMAGE_BASE_URL}${movieDetailModel.posterPath}")
            .error(R.drawable.dummy).placeholder(R.drawable.dummy)
            .into(binding.ivPoster)
        binding.tvTitle.text = movieDetailModel.title
        binding.tvReleaseDate.text =
            getFormattedDate(movieDetailModel.releaseDate, YYYY_MM_DD, DD_MM_YYYY)
        binding.tvRating.text =
            resources.getString(
                R.string.rating,
                String.format("%.2f", movieDetailModel.voteAverage)
            )
        binding.tvPopularity.text = resources.getString(
            R.string.popularity,
            String.format("%.2f", movieDetailModel.popularity)
        )
        binding.tvDetails.text = movieDetailModel.overview
        if (!movieDetailModel.genres.isNullOrEmpty()) {
            addGenreChips(movieDetailModel.genres!!)
        }
    }

    //Add Genre Chips
    private fun addGenreChips(list: List<GenreX>) {
        binding.genreChipGroup.removeAllViews()
        for (ele in list) {
            val tagName: String = ele.name!!
            val chip = Chip(requireContext())
            chip.isClickable = false
            chip.isCheckable = false
            chip.shapeAppearanceModel.withCornerSize(20f)
            val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2f,
                resources.displayMetrics
            ).toInt()
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
            chip.text = tagName
            chip.textSize = 14f
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            chip.chipBackgroundColor =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.genreChipGroup.addView(chip)
        }
    }

    private fun initUi() {
        binding.srlDetail.setOnRefreshListener(this)
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onRefresh() {
        viewModel.callMovieDetails(movieId, checkNetwork(requireContext())) {}
    }

}