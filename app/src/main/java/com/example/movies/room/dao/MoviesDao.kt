package com.example.movies.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.models.MovieListModel

// Movie List Table Dao

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieListModel: List<MovieListModel>)


    @Query("SELECT * FROM movieList WHERE ((genreIds LIKE :genreId || ',%') OR (genreIds LIKE '%,' || :genreId || ',%') OR  (genreIds LIKE '%,' || :genreId) OR (genreIds LIKE :genreId)) ORDER BY popularity ASC")
    fun getMoviesListAscending(genreId: String): PagingSource<Int, MovieListModel>

    @Query("SELECT * FROM movieList ORDER BY popularity ASC")
    fun getMoviesListAscending(): PagingSource<Int, MovieListModel>

    @Query("SELECT * FROM movieList WHERE ((genreIds LIKE :genreId || ',%') OR (genreIds LIKE '%,' || :genreId || ',%') OR  (genreIds LIKE '%,' || :genreId) OR (genreIds LIKE :genreId)) ORDER BY popularity DESC ")
    fun getMoviesListDescending(genreId: String): PagingSource<Int, MovieListModel>

    @Query("SELECT * FROM movieList ORDER BY popularity DESC")
    fun getMoviesListDescending(): PagingSource<Int, MovieListModel>


}