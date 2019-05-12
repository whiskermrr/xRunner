package com.whisker.mrr.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.whisker.mrr.data.converter.CoordsTypeConverter

@Database(
    entities = [

    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(CoordsTypeConverter::class)
abstract class DbRunner : RoomDatabase() {
}