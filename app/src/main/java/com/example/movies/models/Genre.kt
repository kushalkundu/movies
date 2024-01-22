package com.example.movies.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "genre")
data class Genre(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("name") var name: String? = ""
)