package com.pokercc.saveappimage

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.pm.PackageManager
import com.pokercc.saveappimage.database.AppEntity
import com.pokercc.saveappimage.database.AppEntityDao
import com.pokercc.saveappimage.database.AppEntityModule
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class EditAppListViewModel(application: Application) : AndroidViewModel(application) {
    data class AppItem(val appEntity: AppEntity, var selected: Boolean = false)

    /**
     * app列表
     */
    val appItems: MutableLiveData<List<AppItem>> = MutableLiveData()

    enum class SaveAppListState(val message: String? = null) {
        SUCCESS,
        ERROR,
        LOADING
    }

    val saveAppListState: MutableLiveData<SaveAppListState> = MutableLiveData()
    val appEntityDao: AppEntityDao by lazy { AppEntityModule.provideAppEntityDao(application) }

    class EnableAppList(private val appEntityDao: AppEntityDao) {
        private var enableAppIds: List<String>? = null
        private fun getEnableAppIds(context: Context): List<String> {
            return appEntityDao.queryAll()
                .map { it.appId }
        }

        fun enable(context: Context, appId: String): Boolean {
            synchronized(this) {
                if (enableAppIds == null) {
                    enableAppIds = getEnableAppIds(context)
                }
                return enableAppIds!!.contains(appId)
            }
        }
    }

    /**
     * 加载手机上安卓的app列表
     */
    @Suppress("DEPRECATION")
    fun loadInstallApps() {
        val enableAppList = EnableAppList(appEntityDao)
        val packageManager = getApplication<Application>().packageManager
        Flowable
            .defer {
                Flowable.fromIterable(packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES))
            }
            //不是自己
            .filter { it.packageName != getApplication<Application>().packageName }
            .map {
                AppEntity(
                    it.packageName,
                    packageManager.getApplicationLabel(it)?.toString() ?: ""
                )
            }
            .map {
                AppItem(
                    it,
                    enableAppList.enable(getApplication(), it.appId)
                )
            }
            .subscribeOn(Schedulers.io())
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                appItems.value = it
            }
            .subscribe()


    }

    /**
     * 保存app列表
     */
    fun saveAppList() {
        Flowable
            .defer {
                Flowable.fromIterable(appItems.value)
            }
            .doOnSubscribe {
                saveAppListState.postValue(SaveAppListState.LOADING)
            }
            .filter { it.selected }
            .map { it.appEntity }
            .toList()
            .doOnSuccess {
                appEntityDao.clear()
                appEntityDao.insertAll(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                saveAppListState.value = SaveAppListState.SUCCESS
            }
            .doOnError {
                saveAppListState.value = SaveAppListState.ERROR
            }
            .subscribe()

    }


}

