package com.pokercc.saveappimage.plugin

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onLongClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.verticalLayout
import java.io.File

class FunctionDialog(context: Context) : Dialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnkoContext.create(context)
            .apply {
                verticalLayout {
                    button("选择单个View") {
                        onClick {
                            selectSingleView(context.activity())
                            dismiss()
                        }
                    }.lparams {
                        width = MATCH_PARENT
                    }
                    button("选择多个View") {
                        onClick {
                            Toast.makeText(context, "选择多个View", Toast.LENGTH_SHORT).show()
                            dismiss()

                        }
                    }.lparams {
                        width = MATCH_PARENT
                    }
                    button("保存全部") {
                        onClick {
                            saveall(context.activity())
                            dismiss()
                        }
                    }.lparams {
                        width = MATCH_PARENT
                    }
                }
            }
            .view
            .apply {
                setContentView(this)
            }
    }

    /**
     * 选择单个view
     */
    fun selectSingleView(activity: Activity) {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        val subViews = rootView.allViews()
        subViews
            .filter { it is ViewGroup }
            .filter { it !is AdapterView<*> }
            .forEach {
                it.onLongClick {
                    activity.toast("放大ViewGroup")
                    true
                }
                it.isClickable = false
                it.setOnClickListener(null)
            }
        subViews
            .filter { it !is ViewGroup }
            .forEach {
                it.onClick { it ->
                    ViewPropertyDialog(it!!).show()
                }
            }
    }

    /**
     * 保存整个activity里面的drawable
     */
    fun saveall(activity: Activity) {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        val subViews = rootView.allViews()
        var parentFile = File(Environment.getExternalStorageDirectory(), "appDrawables")
        parentFile = File(parentFile, activity.applicationInfo.packageName)
        parentFile = File(parentFile, activity::class.java.simpleName)
        parentFile.mkdirs()

        val progressDialog = ProgressDialog.show(rootView.context, null, "保存中...", true, false)
        // 保存image的图片
        Completable.complete()
            .doOnComplete {
                subViews
                    .filter { it is ImageView }
                    .filter { (it as ImageView).drawable is Drawable }
                    .mapNotNull { (it as ImageView).drawable.toBitmap() }
                    .forEach { it.save(File(parentFile, "img_${it.md5()}.png")) }
                // 保存view 的背景
                subViews
                    .filter { it.background is BitmapDrawable }
                    .mapNotNull { it.background.toBitmap() }
                    .forEach { it.save(File(parentFile, "bg_${it.md5()}.png")) }
                // TextView的其他Drawable
                subViews
                    .filter { it is TextView }
                    .map { (it as TextView).compoundDrawables }
                    .flatMap { it.toList() }
                    .filterNotNull()
                    .mapNotNull { it.toBitmap() }
                    .forEach { it.save(File(parentFile, "cd_${it.md5()}.png")) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                Toast.makeText(rootView.context, "保存成功!", Toast.LENGTH_SHORT).show()
            }
            .doFinally {
                progressDialog.dismiss()
            }
            .subscribe()
    }
}