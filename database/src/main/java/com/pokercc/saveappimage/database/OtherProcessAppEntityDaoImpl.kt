package com.pokercc.saveappimage.database

import android.content.Context
import android.database.Cursor
import android.net.Uri

class OtherProcessAppEntityDaoImpl(private val context: Context) : OtherProcessAppEntityDao {

    override fun queryAll(): List<AppEntity> {
        val appEntities = mutableListOf<AppEntity>()
        val cursor: Cursor? =
            context.contentResolver.query(Uri.parse(APP_LIST_CONTENT_PROVIDER_QUERY_ALL), null, null, null, null)
        cursor?.apply {
            while (cursor.moveToNext()) {
                val appId = cursor.getString(cursor.getColumnIndex(APP_ID))
                val name = cursor.getString(cursor.getColumnIndex(NAME))
                appEntities.add(AppEntity(appId, name))
            }
            close()
        }
        return appEntities
    }
}