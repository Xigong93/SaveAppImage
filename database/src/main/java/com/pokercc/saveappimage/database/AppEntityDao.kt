package com.pokercc.saveappimage.database

import android.arch.persistence.room.*
import android.database.Cursor

@Dao
interface AppEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appEntity: AppEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(appEntities: List<AppEntity>)

    @Delete
    fun delete(appEntity: AppEntity)

    @Delete
    fun deleteAll(appEntities: List<AppEntity>)

    @Query("SELECT * FROM app_entity")
    fun queryAll(): List<AppEntity>

    @Query("SELECT * FROM app_entity")
    fun queryAllToCursor(): Cursor

    @Query("DELETE FROM app_entity")
    fun clear()
}