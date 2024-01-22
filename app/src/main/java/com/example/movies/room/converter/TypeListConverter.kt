package com.example.movies.room.converter

import androidx.room.TypeConverter
import com.example.movies.models.GenreX
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Room DB Type Converter
class TypeListConverter {

    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        if (value == null) {
            return null
        }

        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<Int>?): String? {
        if (value == null) {
            return null
        }

        return Gson().toJson(value)
    }

    @TypeConverter
    fun listToGenreX(list: List<GenreX>?): String {
        return list?.let { Gson().toJson(list) } ?: kotlin.run { "" }
    }

    @TypeConverter
    fun genreXToList(string: String?): List<GenreX>? {
        val typeValue = object :
            TypeToken<List<GenreX>>() {}.type
        return Gson().fromJson<List<GenreX>>(
            string,
            typeValue
        )
    }





}