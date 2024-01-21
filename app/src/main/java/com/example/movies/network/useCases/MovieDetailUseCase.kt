package com.example.movies.network.useCases

import com.example.movies.models.MovieDetailModel
import com.example.movies.network.repository.MovieRepository
import com.example.movies.room.daoImpl.MovieDetailRepo
import com.example.movies.utils.Constants
import javax.inject.Inject

// Movie Detail Api Hit
class MovieDetailUseCase @Inject constructor(private val movieRepository: MovieRepository,
    private val movieDetailRepo: MovieDetailRepo

) {

    suspend operator fun invoke(movieId: String, language: String=Constants.LANGUAGE_EN):MovieDetailModel{
        val response = movieRepository.getMovieDetails(movieId, language)
        movieDetailRepo.insertMovieDetail(response)
        return response
    }
}