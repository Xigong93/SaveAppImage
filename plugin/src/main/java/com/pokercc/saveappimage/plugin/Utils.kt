package com.pokercc.saveappimage.plugin

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import de.robv.android.xposed.XposedBridge
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 * drawable转bitmap
 */
fun Drawable.toBitmap(): Bitmap? {
    if (intrinsicWidth < 0 || intrinsicHeight < 0) {
        return null
    }
    val drawable = mutate()
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth,
        intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    drawable.callback = null
    drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    drawable.draw(Canvas(bitmap))
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

fun getBytesByBitmap(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream(bitmap.byteCount)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

/**
 * 转md5
 */
fun Bitmap.md5(): String {
    val bytes = getBytesByBitmap(this)
    val messageDigest = MessageDigest.getInstance("MD5")
    messageDigest.update(bytes)
    val secretBytes = messageDigest.digest()
    var md5code = BigInteger(1, secretBytes).toString(16);// 16进制数字
    // 如果生成数字未满32位，需要前面补0
    while (md5code.length < 32) {
        md5code += "0"
    }
    return md5code
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