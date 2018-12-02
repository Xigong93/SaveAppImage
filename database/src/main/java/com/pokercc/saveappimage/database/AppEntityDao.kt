package com.pokercc.saveappimage.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.database.Cursor

@Dao
interface AppEntityDao {
    @Insert
    fun insert(appEntity: AppEntity)

    @Insert
    fun insertAll(appEntities: List<AppEntity>)

    @Delete
    fun delete(appEntity: AppEntity)

    @Delete
    fun deleteAll(appEntities: List<AppEntity>)

    @Query("SELECT * FROM app_entity")
    fun queryAll(): List<AppEntity>

    @Query("SELECT * FROM app_entity")
    fun queryAllToCursor(): Cursor
}