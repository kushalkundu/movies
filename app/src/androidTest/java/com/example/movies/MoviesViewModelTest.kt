package com.example.movies

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movies.models.Genre
import com.example.movies.models.MovieDetailModel
import com.example.movies.network.useCases.MoviesUseCase
import com.example.movies.room.AppDatabase
import com.example.movies.room.daoImpl.GenreRepo
import com.example.movies.room.daoImpl.MovieDetailRepo
import com.example.movies.room.daoImpl.MovieListRepo
import com.example.movies.util.getOrAwaitValue
import com.example.movies.viewmodels.MoviesViewModel
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoviesViewModelTest : TestCase() {

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var db: AppDatabase
    private lateinit var moviesUseCase: MoviesUseCase
    private lateinit var movieListRepo: MovieListRepo
    private lateinit var genreRepo: GenreRepo
    private lateinit var movieDetailRepo: MovieDetailRepo


    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
                .build()
        moviesUseCase = mockk<MoviesUseCase>()
        movieListRepo = MovieListRepo(db)
        genreRepo = GenreRepo(db)
        movieDetailRepo = MovieDetailRepo(db)
        moviesViewModel =
            MoviesViewModel(moviesUseCase, movieListRepo, genreRepo, movieDetailRepo, db)
    }


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test_Genre_Repo() {
        moviesViewModel.viewModelScope.launch {
            genreRepo.insertGenre(Genre(id = 1, "Adventure"))
            genreRepo.getGenreList()
        }
        val result = moviesViewModel.genreMutableLiveData.getOrAwaitValue().find {
            it.id == 1 && it.name == "Adventure"
        }
        assertTrue(result != null)
    }

    @Test
    fun test_Movie_Detail_Repo() {
        moviesViewModel.viewModelScope.launch {
            movieDetailRepo.insertMovieDetail(MovieDetailModel(id = 1, title = "The Godfather"))
            moviesViewModel.callMovieDetails(1, false) {}
        }
        assertEquals(1, moviesViewModel.movieDetailMutableLiveData.getOrAwaitValue().id)
    }


}


