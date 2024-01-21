package com.example.movies.models

import com.google.gson.annotations.SerializedName

data class GenreX(
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("name") var name: String? = ""
)