package com.lion.a066ex_animalmanager.data.dao

import androidx.room.TypeConverter
import java.util.Date

// Used for date conversion
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}