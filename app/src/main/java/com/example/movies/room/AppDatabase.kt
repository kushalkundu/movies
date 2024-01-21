package com.example.movies.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.models.Genre
import com.example.movies.models.MovieDetailModel
import com.example.movies.models.MovieListModel
import com.example.movies.room.converter.TypeListConverter
import com.example.movies.room.dao.GenreDao
import com.example.movies.room.dao.MovieDetailDao
import com.example.movies.room.dao.MoviesDao
import com.example.movies.utils.Constants.DB_VERSION

@Database(
    entities = [MovieListModel::class, Genre::class, MovieDetailModel::class],
    version = DB_VERSION)

@TypeConverters(TypeListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun genreDao(): GenreDao
    abstract fun movieDetailDao(): MovieDetailDao
}