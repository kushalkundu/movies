package com.example.movies.network.repository

import androidx.paging.PagingData
import com.example.movies.models.GenreResponse
import com.example.movies.models.MovieDetailModel
import com.example.movies.models.MovieListModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    // For Movie List Api
    suspend fun getMovieList(
        includeAdult: Boolean,
        includeVideo: Boolean,
        language: String,
        region: String,
        withGenres: String
    ): Flow<PagingData<MovieListModel>>

    // For Genre List Api
    suspend fun getGenreList(
        language: String
    ): GenreResponse

    // For Movie Details Api
    suspend fun getMovieDetails(movieId: String, language: String): MovieDetailModel
}