package com.pokercc.saveappimage.database

/**
 * 另外一个进程使用的dao对象，包装了contentProvider
 */
interface OtherProcessAppEntityDao {

    fun queryAll(): List<AppEntity>
}