package com.pokercc.saveappimage.database

import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OtherProcessAppEntityDaoTest {
    lateinit var otherProcessAppEntityDao: OtherProcessAppEntityDao
    lateinit var appEntityDao: AppEntityDao

    @Before
    fun onBefore() {
        otherProcessAppEntityDao = AppEntityModule.provideOtherProcessAppEntityDao(InstrumentationRegistry.getContext())
        appEntityDao = AppEntityModule.provideAppEntityDao(InstrumentationRegistry.getContext())
    }

    @After
    fun onAfter() {
        appEntityDao.clear()
    }

    @Test
    fun testQueryAll01() {
        val appEntities = otherProcessAppEntityDao.queryAll()
        assertTrue(appEntities.isEmpty())
    }

    @Test
    fun testQueryAll02() {
        appEntityDao.insert(AppEntity("a", "b"))
        val appEntities = otherProcessAppEntityDao.queryAll()
        assertTrue(appEntities.size == 1)
        val appEntity = appEntities.first()
        assertTrue(appEntity == AppEntity("a", "b"))

    }
}