package com.example.movies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.models.Genre
import com.example.movies.models.MovieDetailModel
import com.example.movies.models.MovieListModel
import com.example.movies.network.useCases.MoviesUseCase
import com.example.movies.room.AppDatabase
import com.example.movies.room.daoImpl.GenreRepo
import com.example.movies.room.daoImpl.MovieDetailRepo
import com.example.movies.room.daoImpl.MovieListRepo
import com.example.movies.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// Main Activity ViewModel
@HiltViewModel()
class MoviesViewModel @Inject constructor(
    private val moviesUseCase: MoviesUseCase,
    private val movieListRepo: MovieListRepo,
    private val genreRepo: GenreRepo,
    private val movieDetailRepo: MovieDetailRepo,
    database: AppDatabase
) : ViewModel() {

    var genreSelected = 0

    companion object {
        var sortBy: String = Constants.DESCENDING
    }

    val genreMutableLiveData: LiveData<List<Genre>> = database.genreDao().getGenreList()
    val movieDetailMutableLiveData: MutableLiveData<MovieDetailModel> = MutableLiveData()

    private val _state = MutableStateFlow<PagingData<MovieListModel>?>(null)
    val state = _state.asStateFlow()

    // Genre List Api Call
    fun callGenreListApi(isNetworkAvailable: Boolean) {
        if (isNetworkAvailable) {
            viewModelScope.launch {
                val response = moviesUseCase.genreList.invoke()
                if (response.genres.isNotEmpty()) {
                    genreRepo.insertGenre(response.genres)
                }
            }
        }
    }


    // Movie Details Api Call
    fun callMovieDetails(
        movieId: Int,
        isNetworkAvailable: Boolean,
        callFunction: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable) {
                val response = moviesUseCase.movieDetailUseCase.invoke(movieId.toString())
                movieDetailMutableLiveData.postValue(response)
                callFunction(true)
            } else {
                val movieDetailModel = movieDetailRepo.getMovieDetailModel(movieId)
                if (movieDetailModel != null) {
                    movieDetailMutableLiveData.postValue(movieDetailModel)
                    callFunction(true)
                } else {
                    callFunction(false)
                }
            }
        }
    }

    // Movie List Api Call
    fun callMoviesListApi(isNetworkAvailable: Boolean) {
        viewModelScope.launch {
            if (isNetworkAvailable) {
                moviesUseCase.movieListUseCase.invoke(
                    withGenres = genreSelected.toString())
                    .cachedIn(viewModelScope).collect { pagingModel ->
                        _state.emit(pagingModel)

                    }
            } else {

                Pager(
                    PagingConfig(
                        pageSize = 4, enablePlaceholders = true, initialLoadSize = 2
                    ),
                ) {
                    movieListRepo.getMoviesList(genreSelected, sortBy)
                }.flow.cachedIn(viewModelScope).collectLatest { pagingModel ->
                    _state.emit(pagingModel)

                }


            }
        }
    }


}