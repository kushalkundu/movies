package com.example.movies.room.daoImpl

import androidx.paging.PagingSource
import com.example.movies.models.MovieListModel
import com.example.movies.room.AppDatabase
import com.example.movies.utils.Constants.ASCENDING
import javax.inject.Inject

// Movie List Dao Repo
class MovieListRepo @Inject constructor(private val database: AppDatabase) {


    suspend fun insertMovie(movieListModel: List<MovieListModel>) {
        database.moviesDao().insertMovie(movieListModel)
    }


    fun getMoviesList(genreId: Int, sortBy: String): PagingSource<Int, MovieListModel> {
        return if (genreId == 0) {
            if (sortBy == ASCENDING) database.moviesDao()
                .getMoviesListAscending() else database.moviesDao().getMoviesListDescending()
        } else {
            if (sortBy == ASCENDING) database.moviesDao()
                .getMoviesListAscending(genreId.toString()) else
                database.moviesDao().getMoviesListDescending(genreId.toString())
        }
    }
}