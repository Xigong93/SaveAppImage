package com.pokercc.saveappimage.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * app数据类
 */
@Entity(tableName = "app_entity")
data class AppEntity(
    /**
     *包名
     */
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = APP_ID)
    val appId: String,
    /**
     * 名字
     */
    @ColumnInfo(name = NAME)
    val name: String
)