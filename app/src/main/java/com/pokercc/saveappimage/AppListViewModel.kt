package com.pokercc.saveappimage

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.pokercc.saveappimage.database.AppEntityDao
import com.pokercc.saveappimage.database.AppEntityModule
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AppListViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * app列表
     */
    val appEntities: MutableLiveData<List<AppWithIcon>> = MutableLiveData()

    val appEntityDao: AppEntityDao by lazy { AppEntityModule.provideAppEntityDao(application) }
    var disposable: Disposable? = null
    /**
     * 加载app列表
     */
    fun loadAppList() {
        val packageManager = getApplication<Application>().packageManager

        disposable = Flowable
            .defer {
                Flowable.fromIterable(appEntityDao.queryAll())
            }
            .map {
                AppWithIcon(it, packageManager.getApplicationIcon(it.appId))
            }
            .subscribeOn(Schedulers.io())
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> appEntities.setValue(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}

