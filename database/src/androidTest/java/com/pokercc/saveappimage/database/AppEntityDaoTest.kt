package com.pokercc.saveappimage.database

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
//        assertTrue
        assertTrue(appEntityDao.queryAll().isEmpty())
        appEntityDao.insert(AppEntity("a", "A"))
        assertTrue(appEntityDao.queryAll().size == 1)

    }

    @Test
    fun testInsertAll() {
        assertTrue(appEntityDao.queryAll().isEmpty())
        val count = 10
        val list = (0 until count)
            .map { it.toString() }
            .map { AppEntity(it, it) }
            .toList()
        appEntityDao.insertAll(list)
        assertTrue(appEntityDao.queryAll().size == count)

    }


    @Test
    fun testDelete() {
        val a = AppEntity("a", "A")
        appEntityDao.insert(a)
        assertTrue(appEntityDao.queryAll().size == 1)
        appEntityDao.delete(a)
        assertTrue(appEntityDao.queryAll().isEmpty())
    }

    @Test
    fun testDeleteAll() {
        val count = 10
        val list = (0 until count)
            .map { it.toString() }
            .map { AppEntity(it, it) }
            .toList()
        appEntityDao.insertAll(list)
        assertTrue(appEntityDao.queryAll().size == count)
        appEntityDao.deleteAll(list)
        assertTrue(appEntityDao.queryAll().isEmpty())
    }

    @Test
    fun testQuery() {
        assertTrue(appEntityDao.queryAll().isEmpty())
        val a = AppEntity("a", "A")
        appEntityDao.insert(a)
        assertTrue(appEntityDao.queryAll().size == 1)
        for (i in 0 until 10) {
            appEntityDao.insert(AppEntity(i.toString(), i.toString()))
        }
        assertTrue(appEntityDao.queryAll().size == 11)
    }

    @Test
    fun testQueryAllToCursor() {
        val count = 10
        for (i in 0 until count) {
            appEntityDao.insert(AppEntity(i.toString(), i.toString()))
        }
        assertEquals(appEntityDao.queryAllToCursor().count, count)
    }
}