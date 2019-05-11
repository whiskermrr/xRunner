package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.converter.CoordsTypeConverter

@Database(
    entities = [

    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(CoordsTypeConverter::class)
abstract class DbRunner : RoomDatabase() {
}