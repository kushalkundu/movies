package com.example.movies.network.useCases

import com.example.movies.network.repository.MovieRepository
import com.example.movies.utils.Constants
import javax.inject.Inject

// Movie List Api Hit
class MovieListUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        includeAdult: Boolean = false,
        includeVideo: Boolean = false,
        language: String = Constants.LANGUAGE_EN,
        region: String = Constants.REGION,
        withGenres: String
    ) = movieRepository.getMovieList(
        includeAdult,
        includeVideo,
        language,
        region,
        withGenres
    )


}