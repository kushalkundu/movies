package com.example.movies.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonInclude
import com.google.gson.annotations.SerializedName

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity("movieDetail")
data class MovieDetailModel(
    @SerializedName("adult") var adult: Boolean? = false,
    @SerializedName("backdrop_path") var backdropPath: String? = "",
    @SerializedName("budget") var budget: Int? = 0,
    @SerializedName("genres") var genres: List<GenreX>? = emptyList(),
    @SerializedName("homepage") var homepage: String? = "",
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") var id: Int,
    @SerializedName("imdb_id") var imdbId: String? = "",
    @SerializedName("original_language") var originalLanguage: String? = "",
    @SerializedName("original_title") var originalTitle: String? = "",
    @SerializedName("overview") var overview: String? = "",
    @SerializedName("popularity") var popularity: Double? = 0.0,
    @SerializedName("poster_path") var posterPath: String? = "",
    @SerializedName("release_date") var releaseDate: String? = "",
    @SerializedName("revenue") var revenue: Int? = 0,
    @SerializedName("runtime") var runtime: Int? = 0,
    @SerializedName("status") var status: String? = "",
    @SerializedName("tagline") var tagline: String? = "",
    @SerializedName("title") var title: String? = "",
    @SerializedName("video") var video: Boolean? = false,
    @SerializedName("vote_average") var voteAverage: Double? = 0.0,
    @SerializedName("vote_count") var voteCount: Int? = 0
)