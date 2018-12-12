package com.pokercc.saveappimage.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

private const val DATABASE_FILE_NAME = "app_entity"

@Database(entities = [AppEntity::class], exportSchema = true, version = 1)
internal abstract class AppEntityDatabase : RoomDatabase() {
    abstract fun appEntityDao(): AppEntityDao

    companion object {

        private var instance: AppEntityDatabase? = null
        fun getInstance(context: Context): AppEntityDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppEntityDatabase {
            return Room
                .databaseBuilder<AppEntityDatabase>(context, AppEntityDatabase::class.java, DATABASE_FILE_NAME)
                .allowMainThreadQueries()
                .build()
        }
    }
}