package com.whisker.mrr.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Preferences")
data class Preferences(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COL_KEY)
    val key: String,

    @ColumnInfo(name = COL_STRING_VALUE)
    val stringValue: String? = null,

    @ColumnInfo(name = COL_LONG_VALUE)
    val longValue: Long? = null,

    @ColumnInfo(name = COL_BOOLEAN_VALUE)
    val booleanValue: Boolean? = null
) {
    companion object {
        const val COL_KEY = "key"
        const val COL_STRING_VALUE = "stringValue"
        const val COL_LONG_VALUE = "longValue"
        const val COL_BOOLEAN_VALUE = "booleanValue"
    }
}