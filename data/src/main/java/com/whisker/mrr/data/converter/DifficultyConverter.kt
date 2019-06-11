package com.whisker.mrr.data.converter

import androidx.room.TypeConverter
import com.whisker.mrr.domain.model.ChallengeDifficulty

class DifficultyConverter {

    @TypeConverter
    fun toInt(difficulty: ChallengeDifficulty) : Int {
        return when(difficulty) {
            ChallengeDifficulty.EASY -> 0
            ChallengeDifficulty.NORMAL -> 1
            ChallengeDifficulty.HARD -> 2
            else -> 0
        }
    }

    @TypeConverter
    fun fromInt(difficulty: Int) : ChallengeDifficulty {
        return when(difficulty) {
            0 -> ChallengeDifficulty.EASY
            1 -> ChallengeDifficulty.NORMAL
            2 -> ChallengeDifficulty.HARD
            else -> ChallengeDifficulty.EASY
        }
    }
}