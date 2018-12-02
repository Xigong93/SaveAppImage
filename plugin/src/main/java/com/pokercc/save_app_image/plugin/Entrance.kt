package com.pokercc.save_app_image.plugin


import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File
import java.io.FileOutputStream
import java.util.*


const val TARGET_PACKAGE_NAME = "com.pokercc.textdemo"

/**
 * 插件入口
 */
class Entrance : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {

        XposedBridge.log("handleLoadPackage执行啦!")
        if (TARGET_PACKAGE_NAME == lpparam?.packageName) {
            XposedBridge.log("开始hook测试程序!")
            hookTestApp(lpparam)
        }
    }

    private fun hookTestApp(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("注册activity hook")
        XposedHelpers.findAndHookMethod(Activity::class.java,
            "onResume",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    val activity = param!!.thisObject as Activity
                    hookActivity(activity)
                }
            })
    }

    private fun hookActivity(activity: Activity) {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        val button = Button(activity)
            .apply {
                text = "保存drawable"
                setOnClickListener {
                    saveDrawable(activity)
                }
            }
        rootView.post {
            PopupWindow(
                button, 300, 200
            ).showAtLocation(rootView, Gravity.RIGHT or Gravity.BOTTOM, 0, 0)
        }
    }

    private fun saveDrawable(activity: Activity) {
        XposedBridge.log("开始保存图片")
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        val subViews = rootView.allViews()
        val parentFile = Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES)
        subViews
            .filter { it is ImageView }
            .filter { it.background != null }
            .map { it.background.toBitmap() }
            .forEach { it.save(File(parentFile, UUID.randomUUID().toString() + ".png")) }
    }

    /**
     * drawable转bitmap
     */
    private fun Drawable.toBitmap(): Bitmap {
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
    private fun Bitmap.save(file: File) {
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
    private fun ViewGroup.allViews(): List<View> {
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
}