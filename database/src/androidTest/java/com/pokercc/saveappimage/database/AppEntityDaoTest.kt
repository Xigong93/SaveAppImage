package com.pokercc.saveappimage.database

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import org.junit.Before
import org.junit.Test

class AppEntityDaoTest {


    lateinit var appEntityDao: AppEntityDao
    @Before
    fun onBefore() {
        val context = InstrumentationRegistry.getContext()
        appEntityDao = Room.inMemoryDatabaseBuilder(context, AppEntityDatabase::class.java).build().appEntityDao()
    }

    @Test
    fun testInsert() {
        assert(appEntityDao.queryAll().isEmpty()) { "默认数量应该是0" }
        appEntityDao.insert(AppEntity("a", "A"))
        assert(appEntityDao.queryAll().size == 1) { "插入一条后数量应该是1" }

    }

    @Test
    fun testInsertAll() {
        assert(appEntityDao.queryAll().isEmpty())
        val count = 10
        val list = (0 until count)
            .map { it.toString() }
            .map { AppEntity(it, it) }
            .toList()
        appEntityDao.insertAll(list)
        assert(appEntityDao.queryAll().size == count)

    }


    @Test
    fun testDelete() {
        val a = AppEntity("a", "A")
        appEntityDao.insert(a)
        assert(appEntityDao.queryAll().size == 1)
        appEntityDao.delete(a)
        assert(appEntityDao.queryAll().isEmpty())
    }

    @Test
    fun testDeleteAll() {
        val count = 10
        val list = (0 until count)
            .map { it.toString() }
            .map { AppEntity(it, it) }
            .toList()
        appEntityDao.insertAll(list)
        assert(appEntityDao.queryAll().size == count)
        appEntityDao.deleteAll(list)
        assert(appEntityDao.queryAll().isEmpty())
    }

    @Test
    fun testQuery() {
        assert(appEntityDao.queryAll().isEmpty())
        val a = AppEntity("a", "A")
        appEntityDao.insert(a)
        assert(appEntityDao.queryAll().size == 1)
        for (i in 0 until 10) {
            appEntityDao.insert(AppEntity(i.toString(), i.toString()))
        }
        assert(appEntityDao.queryAll().size == 11)
    }

    @Test
    fun testQueryAllToCursor() {
        assert(appEntityDao.queryAll().isEmpty())
        val a = AppEntity("a", "A")
        appEntityDao.insert(a)
        assert(appEntityDao.queryAll().size == 1)
        for (i in 0 until 10) {
            appEntityDao.insert(AppEntity(i.toString(), i.toString()))
        }
        assert(appEntityDao.queryAllToCursor().columnCount == 11)
    }
}