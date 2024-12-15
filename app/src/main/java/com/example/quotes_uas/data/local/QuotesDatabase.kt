package com.example.quotes_uas.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuotesEntity::class], version = 8, exportSchema = false)
abstract class QuotesDatabase : RoomDatabase() {
    abstract fun quotesDao(): QuotesDao
    companion object {
        @Volatile
        private var INSTANCE: QuotesDatabase? = null

        fun getDatabase(context: Context): QuotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuotesDatabase::class.java,
                    "quotes_database"
                ) .fallbackToDestructiveMigration() // Tambahkan ini untuk menghapus database lama saat terjadi perubahan skema
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}