package com.example.movies.room.daoImpl

import androidx.lifecycle.LiveData
import com.example.movies.models.Genre
import com.example.movies.room.AppDatabase
import javax.inject.Inject

// Genre Dao Repo
class GenreRepo @Inject constructor(private val database: AppDatabase) {
    suspend fun insertGenre(genre: Genre) {
        database.genreDao().insertGenre(genre = genre)
    }

    suspend fun insertGenre(genre: List<Genre>) {
        database.genreDao().insertGenre(genre = genre)
    }

    fun getGenreList(): LiveData<List<Genre>> {
        return database.genreDao().getGenreList()
    }
}