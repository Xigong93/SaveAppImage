package com.pokercc.saveappimage

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class AppListViewModel : ViewModel() {

    /**
     * app列表
     */
    val appEntities: LiveData<List<AppWithIcon>> = MutableLiveData()
}

