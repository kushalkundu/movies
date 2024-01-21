package com.example.movies.models

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres") var genres: List<Genre> = emptyList()
)