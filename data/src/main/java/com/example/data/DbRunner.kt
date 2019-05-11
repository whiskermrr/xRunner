package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [

    ],
    version = 1,
    exportSchema = true
)
abstract class DbRunner : RoomDatabase() {
}