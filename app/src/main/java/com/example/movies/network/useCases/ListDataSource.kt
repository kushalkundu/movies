package com.example.movies.network.useCases

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.models.MovieListModel
import com.example.movies.network.ApiInterface
import com.example.movies.room.daoImpl.MovieListRepo
import com.example.movies.utils.Constants
import com.example.movies.utils.Constants.ASCENDING
import com.example.movies.utils.Constants.NULL
import com.example.movies.viewmodels.MoviesViewModel

// Movie List Api Hit Pagination and store in Room DB
class ListDataSource(
    private val apiInterface: ApiInterface,
    private val movieListRepo: MovieListRepo,
    private val includeAdult: Boolean = true,
    private val includeVideo: Boolean = true,
    private val language: String = Constants.LANGUAGE_EN,
    private val region: String = Constants.REGION,
    private val withGenres: String
) : PagingSource<Int, MovieListModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListModel> {
        return try {

            val pageNumber = params.key ?: 1 // current page that is being displayed
            val response = apiInterface.getMoviesList(
                includeAdult,
                includeVideo,
                language,
                pageNumber.toString(),
                region,
                MoviesViewModel.sortBy,
                withGenres = if (withGenres.toInt() == 0) NULL else withGenres
            )

            val data = response.results
            movieListRepo.insertMovie(data)


            LoadResult.Page(
                data = if (MoviesViewModel.sortBy == ASCENDING) data.sortedBy { it.popularity } else data.sortedByDescending { it.popularity },
                prevKey = null,
                nextKey = if (data.isEmpty()) null else pageNumber + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, MovieListModel>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}