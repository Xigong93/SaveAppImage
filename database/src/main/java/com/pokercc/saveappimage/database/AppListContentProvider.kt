package com.pokercc.saveappimage.database

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

const val APP_LIST_CONTENT_PROVIDER_QUERY_ALL = "content://com.pokercc.saveappimage.database.AppListContentProvider/app_list/list"

/**
 * app列表的内容提供者
 */
class AppListContentProvider : ContentProvider() {

    private val appEntityDao: AppEntityDao by lazy {
        AppEntityModule.provideAppEntityDao(context!!)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null

    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        return null

    }

    override fun onCreate(): Boolean {

        return true

    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return appEntityDao.queryAllToCursor()

    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0

    }
}
