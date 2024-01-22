package com.example.movies.room.daoImpl

import com.example.movies.models.MovieDetailModel
import com.example.movies.room.AppDatabase
import javax.inject.Inject

// Movie Detail Dao Repo

class MovieDetailRepo @Inject constructor(private val database: AppDatabase) {

    suspend fun insertMovieDetail(movieDetailModel: MovieDetailModel) {
        database.movieDetailDao().insertMovieDetails(movieDetailModel)
    }

    suspend fun getMovieDetailModel(movieId: Int): MovieDetailModel {
        return database.movieDetailDao().getMovieDetails(movieId)
    }
}