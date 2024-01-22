package com.example.movies.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.models.MovieDetailModel

// Movie Details Table Dao

@Dao
interface MovieDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movieDetailModel: MovieDetailModel)

    @Query("SELECT * FROM movieDetail WHERE id = :movieId")
    suspend fun getMovieDetails(movieId: Int): MovieDetailModel

}