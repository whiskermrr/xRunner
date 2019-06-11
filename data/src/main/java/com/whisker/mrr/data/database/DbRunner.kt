package com.whisker.mrr.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.whisker.mrr.data.converter.CoordsTypeConverter
import com.whisker.mrr.data.converter.DifficultyConverter
import com.whisker.mrr.data.database.dao.ChallengeDao
import com.whisker.mrr.data.database.dao.PreferencesDao
import com.whisker.mrr.data.database.dao.RouteDao
import com.whisker.mrr.data.database.dao.UserStatsDao
import com.whisker.mrr.data.database.model.ChallengeEntity
import com.whisker.mrr.data.database.model.Preferences
import com.whisker.mrr.data.database.model.RouteEntity
import com.whisker.mrr.data.database.model.UserStatsEntity

@Database(
    entities = [
        ChallengeEntity::class,
        RouteEntity::class,
        UserStatsEntity::class,
        Preferences::class
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
                        instance = Room.databaseBuilder(context, DbRunner::class.java, DB_ROOM_NAME).build()
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
}