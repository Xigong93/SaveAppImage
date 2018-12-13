package com.pokercc.saveappimage.plugin


import android.app.Activity
import android.app.AndroidAppHelper
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import com.pokercc.saveappimage.database.AppEntityModule
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference


/**
 * 插件入口
 */
class Entrance : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        XposedBridge.log("handleLoadPackage执行啦!")

        getContext(lpparam)
    }

    private fun getContext(lpparam: XC_LoadPackage.LoadPackageParam) {

        try {
            val contextClass = findClass("android.content.ContextWrapper", lpparam.classLoader)
            findAndHookMethod(contextClass, "getApplicationContext", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    param?.apply {
                        XposedBridge.log("CSDN_LQR-->得到上下文")

                        val appEntities = AppEntityModule.provideOtherProcessAppEntityDao(result as Context).queryAll()
                        val appIdSet = appEntities
                            .map { it.appId }
                            .toSet()
                        XposedBridge.log("开启的app列表:$appIdSet")
                        if (appIdSet.contains(lpparam.packageName)) {
                            XposedBridge.log("开始hook:${lpparam.packageName}")
                            hookTestApp(lpparam)
                        }
                    }


                }
            });
        } catch (t: Throwable) {
            XposedBridge.log("CSDN_LQR-->获取上下文出错");
            XposedBridge.log(t)
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
        val button = Button(activity).apply {
            text = "保存drawable"
            setBackgroundColor(Color.YELLOW)
            setOnClickListener {
                saveDrawable(activity)
            }
        }
        val layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, 200).also {
            it.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            it.setMargins(30, 30, 30, 30)
        }
        (rootView.rootView as ViewGroup).addView(button, layoutParams)
    }

    private fun saveDrawable(activity: Activity) {
        XposedBridge.log("开始保存图片")
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        val subViews = rootView.allViews()

        var parentFile = File(Environment.getExternalStorageDirectory(), "appDrawables")
        parentFile = File(parentFile, activity.applicationInfo.packageName)
        parentFile = File(parentFile, activity::class.java.simpleName)
        parentFile.mkdirs()

        // 保存image的图片
        subViews
            .filter { it is ImageView }
            .filter { (it as ImageView).drawable is BitmapDrawable }
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
        Toast.makeText(rootView.context, "保存成功!", Toast.LENGTH_SHORT).show()
    }


}