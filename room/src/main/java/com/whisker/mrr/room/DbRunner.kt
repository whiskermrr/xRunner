package com.whisker.mrr.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.whisker.mrr.room.converter.CoordsTypeConverter
import com.whisker.mrr.room.converter.DifficultyConverter
import com.whisker.mrr.room.dao.*
import com.whisker.mrr.room.model.*

@Database(
    entities = [
        ChallengeEntity::class,
        RouteEntity::class,
        UserStatsEntity::class,
        Preferences::class,
        ChallengeProgressEntity::class,
        UserStatsProgressEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(CoordsTypeConverter::class, DifficultyConverter::class)
abstract class DbRunner : RoomDatabase() {

    companion object {
        private const val DB_ROOM_NAME = "xrunner_room.db"

        @Volatile
        private var instance: DbRunner? = null

        fun getInstance(context: Context): DbRunner {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(context, DbRunner::class.java,
                            DB_ROOM_NAME
                        ).build()
                    }
                }
            }

            return instance!!
        }
    }

    abstract fun challengeDao() : ChallengeDao
    abstract fun routeDao() : RouteDao
    abstract fun userStatsDao() : UserStatsDao
    abstract fun preferencesDao() : PreferencesDao
    abstract fun challengeProgressDao() : ChallengeProgressDao
    abstract fun userStatsProgressDao() : UserStatsProgressDao
}