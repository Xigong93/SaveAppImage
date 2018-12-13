package com.pokercc.saveappimage.plugin


import android.app.Activity
import android.app.AndroidAppHelper
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import com.pokercc.saveappimage.database.AppEntityModule
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        if (rootView.findViewWithTag<View>(BUTTON_TAG) != null) {
            return
        }
        val button = Button(activity).apply {
            text = "保存drawable"
            tag = BUTTON_TAG
            setPadding(10, 5, 10, 5)
            setBackgroundColor(Color.YELLOW)
            setOnClickListener {
                FunctionDialog(activity).show()
            }
        }

        (rootView.rootView as ViewGroup)
            .addView(button, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                .also {
                    it.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    it.bottomMargin = 30
                })
    }


}