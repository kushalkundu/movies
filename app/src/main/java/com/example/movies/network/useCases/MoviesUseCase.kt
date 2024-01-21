package com.example.movies.network.useCases


// App UseCases
data class MoviesUseCase(
    val movieListUseCase: MovieListUseCase,
    val genreList: GenreListUseCase,
    val movieDetailUseCase:MovieDetailUseCase
)