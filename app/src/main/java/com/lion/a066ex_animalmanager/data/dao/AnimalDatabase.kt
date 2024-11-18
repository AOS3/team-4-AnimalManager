package com.lion.a066ex_animalmanager.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lion.a066ex_animalmanager.data.vo.AnimalVO


@Database(entities = [AnimalVO::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AnimalDatabase : RoomDatabase() {
    abstract fun animalDAO(): AnimalDAO

    companion object {
        var animalDatabase: AnimalDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AnimalDatabase? {
            synchronized(AnimalDatabase::class) {
                animalDatabase = Room.databaseBuilder(
                    context.applicationContext, AnimalDatabase::class.java,
                    "Animal.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return animalDatabase
        }
    }


    // Not used
//    fun destroyInstance() {
//        animalDatabase = null
//    }
}

