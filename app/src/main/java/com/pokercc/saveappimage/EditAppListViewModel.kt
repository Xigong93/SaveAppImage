package com.pokercc.saveappimage

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class EditAppListViewModel(application: Application) : AndroidViewModel(application) {
    data class AppItem(val appEntity: AppEntity, var selected: Boolean)

    /**
     * app列表
     */
    val appItems: MutableLiveData<List<AppItem>> = MutableLiveData()

    private var disposed: Disposable? = null
    /**
     * 加载手机上安卓的app列表
     */
    fun loadInstallApps() {
        disposed = Flowable
            .defer { Flowable.just(getInstallApps()) }
            .subscribeOn(Schedulers.io())
            .subscribe {
                appItems.postValue(it)
            }

    }

    private fun getInstallApps(): List<AppItem> {
        val packageManager = getApplication<Application>().packageManager
        return packageManager
            .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES)
            .map {
                AppEntity(
                    it.packageName,
                    packageManager.getApplicationLabel(it)?.toString() ?: "",
                    it.loadIcon(packageManager)
                )
            }
            .map { AppItem(it, false) }

    }

    override fun onCleared() {
        super.onCleared()
        disposed?.dispose()
    }

}

