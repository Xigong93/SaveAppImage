package com.pokercc.saveappimage.plugin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * 显示view的属性
 */
class ViewPropertyDialog(private val target: View) :
    Dialog(target.context, android.R.style.Theme_DeviceDefault_Light_Dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnkoContext.create(context).apply {
            verticalLayout {
                textView("类名:${target.javaClass.name}")
                textView("类型:${target.viewType}")
                textView("宽:${target.width}px")
                textView("高:${target.height}px")
                button("详情") {
                    onClick {
                        target.viewType.showProp(target)
                    }
                }
                button("全部属性") {
                    onClick {
                        dismiss()
                        AlertDialog.Builder(context)
                            .setItems(target.javaClass.declaredFields.map { it ->
                                it.isAccessible = true
                                "${it.name}:${it.type}"
                            }.toTypedArray(), null)
                            .show()

                    }
                }
            }
        }.view.apply { setContentView(this) }
    }

}

enum class ViewType(val desc: String) {
    VIEW("View") {
        override fun showProp(view: View) {
        }
    },
    IMAGE_VIEW("ImageView") {
        override fun showProp(view: View) {
            ImageViewDialog(view as ImageView).show()
        }
    },
    TEXT_VIEW("TextView") {
        override fun showProp(view: View) {
        }
    },
    VIEW_GROUP("ViewGroup") {
        override fun showProp(view: View) {

        }
    };

    override fun toString(): String {
        return desc
    }

    abstract fun showProp(view: View)

}

val View.viewType: ViewType
    get() = when (this) {
        is TextView -> ViewType.TEXT_VIEW
        is ImageView -> ViewType.IMAGE_VIEW
        is ViewGroup -> ViewType.VIEW_GROUP
        else -> ViewType.VIEW
    }

class ImageViewDialog(private val target: ImageView) :
    Dialog(target.context, android.R.style.Theme_DeviceDefault_Light_Dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnkoContext.create(context).apply {
            verticalLayout {
                textView("类名:${target.javaClass.name}")
                textView("背景")
                imageView(target.backgroundDrawable)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView("前景")
                    imageView(target.foreground)
                }
                textView("图片")
                imageView(target.image)
                button("全部属性") {
                    onClick {
                        dismiss()
                        AlertDialog.Builder(context)
                            .setItems(target.javaClass.declaredFields.map { it ->
                                it.isAccessible = true
                                "${it.name}:${it.type}"
                            }.toTypedArray(), null)
                            .show()

                    }
                }
            }
        }.view.apply { setContentView(this) }
    }

}
