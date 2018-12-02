package com.pokercc.saveappimage.plugin

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import de.robv.android.xposed.XposedBridge
import java.io.File
import java.io.FileOutputStream

/**
 * drawable转bitmap
 */
 fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth,
        intrinsicHeight,
        if (opacity != PixelFormat.OPAQUE)
            Bitmap.Config.ARGB_8888
        else
            Bitmap.Config.RGB_565
    )
    setBounds(0, 0, intrinsicWidth, intrinsicHeight);
    draw(Canvas(bitmap))
    return bitmap
}

/**
 * 保存bitmap
 */
 fun Bitmap.save(file: File) {
    file.parentFile?.mkdirs()
    var outputStream: FileOutputStream? = null
    try {
        outputStream = file.outputStream()
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        XposedBridge.log("开始保存$file 成功")
    } catch (e: Exception) {
        outputStream?.close()
        XposedBridge.log("开始保存$file 失败")
    } finally {
        recycle()
    }

}


/**
 * 递归查找全部的子view
 */
 fun ViewGroup.allViews(): List<View> {
    val views = mutableListOf<View>()
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        views.add(child)
        if (child is ViewGroup) {
            views.addAll(child.allViews())
        }
    }
    return views.toList()
}