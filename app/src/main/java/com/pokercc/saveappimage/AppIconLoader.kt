package com.pokercc.saveappimage

import android.widget.ImageView
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * appIcon加载器
 */
object AppIconLoader {
    private val singleExecutor: Executor = Executors.newSingleThreadExecutor()

    /**
     * 异步加载App的Icon，设置到ImageView上,处理了列表错位的问题
     */
    fun loadAppIcon(appId: String, imageViewRef: WeakReference<ImageView>) {
        val packageManager = imageViewRef.get()!!.context.applicationContext.packageManager
        imageViewRef.get()?.setTag(R.id.ImageViewTagAppId, appId)
        Maybe
            .defer {
                Maybe.just(packageManager.getApplicationIcon(appId))
            }
            .subscribeOn(Schedulers.from(singleExecutor))
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess {
                imageViewRef.get()?.apply {
                    if (getTag(R.id.ImageViewTagAppId) == appId) {
                        setImageDrawable(it)
                    }
                }

            }
            .subscribe()

    }

}