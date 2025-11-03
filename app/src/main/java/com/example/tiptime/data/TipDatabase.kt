package com.example.tiptime.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tip::class], version = 1, exportSchema = false)
abstract class TipDatabase : RoomDatabase() {

    abstract fun tipDao(): TipDao

    companion object {
        @Volatile
        private var Instance: TipDatabase? = null

        fun getDatabase(context: Context): TipDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TipDatabase::class.java, "tip_database")
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}