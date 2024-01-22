package com.example.movies.network.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movies.models.GenreResponse
import com.example.movies.models.MovieDetailModel
import com.example.movies.models.MovieListModel
import com.example.movies.network.ApiInterface
import com.example.movies.network.URL
import com.example.movies.network.repository.MovieRepository
import com.example.movies.network.useCases.ListDataSource
import com.example.movies.room.daoImpl.MovieListRepo
import com.example.movies.utils.Constants.LANGUAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface, private val moviesRepo: MovieListRepo
) : MovieRepository {

    // For Movie List Api
    override suspend fun getMovieList(
        includeAdult: Boolean,
        includeVideo: Boolean,
        language: String,
        region: String,
        withGenres: String
    ): Flow<PagingData<MovieListModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                6,
                enablePlaceholders = true,
                initialLoadSize = 50
            ), pagingSourceFactory = {
                ListDataSource(
                    apiInterface, moviesRepo,
                    includeAdult, includeVideo, language, region, withGenres
                )
            }
        ).flow

    }


    // For Genre List Api
    override suspend fun getGenreList(language: String): GenreResponse {
        return apiInterface.getGenreList(language = language)
    }

    // For Movie Details Api
    override suspend fun getMovieDetails(movieId: String, language: String): MovieDetailModel {
        return apiInterface.getMovieDetails("${URL.BASE_URL}${URL.MOVIE_DETAILS}$movieId?${LANGUAGE}=${language}")
    }
}