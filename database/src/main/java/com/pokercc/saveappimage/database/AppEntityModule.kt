package com.pokercc.saveappimage.database

import android.content.Context

object AppEntityModule {
    /**
     * 提供默认的dao对象
     */
    internal fun provideAppEntityDao(context: Context) = AppEntityDatabase.getInstance(context).appEntityDao()

    /**
     * 提供另外一个进程可使用的dao对象
     */
    fun provideOtherProcessAppEntityDao(context: Context) = OtherProcessAppEntityDaoImpl(context)

}