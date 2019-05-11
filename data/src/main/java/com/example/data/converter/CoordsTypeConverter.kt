package com.example.data.converter

import androidx.room.TypeConverter
import com.example.data.fromJson
import com.google.gson.Gson
import com.whisker.mrr.domain.model.Coords

class CoordsTypeConverter {

    @TypeConverter
    fun toJson(coords: List<Coords>) : String {
        return Gson().toJson(coords)
    }

    @TypeConverter
    fun fromJson(json: String) : List<Coords> {
        return Gson().fromJson<List<Coords>>(json)
    }
}