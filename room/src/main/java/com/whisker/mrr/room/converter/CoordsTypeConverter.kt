package com.whisker.mrr.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.room.fromJson

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