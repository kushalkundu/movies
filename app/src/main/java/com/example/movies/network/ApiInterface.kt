package com.example.movies.network

import com.example.movies.models.GenreResponse
import com.example.movies.models.MovieDetailModel
import com.example.movies.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit Api Interface
interface ApiInterface {

    // Fetch Movie List
    @GET(URL.DISCOVER_MOVIES)
    suspend fun getMoviesList(
        @Query("include_adult") includeAdult: Boolean,
        @Query("include_video") includeVideo: Boolean,
        @Query("language") language: String,
        @Query("page") page: String,
        @Query("region") region: String,
        @Query("sort_by") sortBy: String,
        @Query("with_genres") withGenres: String?,
    ): MovieResponse

    // Fetch Genre List
    @GET(URL.GENRE_LIST)
    suspend fun getGenreList(
        @Query("language") language: String
    ): GenreResponse

    // Fetch Movie Details
    @GET
    suspend fun getMovieDetails(
        @retrofit2.http.Url fullUrl: String
    ): MovieDetailModel
}