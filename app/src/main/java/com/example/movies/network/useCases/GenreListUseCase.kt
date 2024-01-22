package com.example.movies.network.useCases

import com.example.movies.models.GenreResponse
import com.example.movies.network.repository.MovieRepository
import com.example.movies.utils.Constants
import javax.inject.Inject

// Genre List Api Hit
class GenreListUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(language: String = Constants.LANGUAGE_EN): GenreResponse {
        return movieRepository.getGenreList(language = language)
    }
}