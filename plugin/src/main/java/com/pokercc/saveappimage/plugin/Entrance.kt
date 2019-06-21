package com.pokercc.saveappimage.plugin


import android.app.Activity
import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.pokercc.saveappimage.database.AppEntityModule
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.ele.uetool.UETool


/**
 * 插件入口
 */
class Entrance : IXposedHookLoadPackage {
    private val activityHook = ActivityHook()
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        log("handleLoadPackage执行啦!")

        hookApplicationOnCreate(lpparam)
//        getContext(lpparam)
    }

    private fun log(message: String) {
        XposedBridge.log(message)
    }
    private fun log(throwable: Throwable) {
        XposedBridge.log(throwable)
    }


    private fun hookApplicationOnCreate(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            val contextClass = findClass(Application::class.java.name, lpparam.classLoader)
            findAndHookMethod(contextClass, "onCreate", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    param?.apply {

                        val app = thisObject as Context

                        log("得到Context:$app")
                        // 只Hook 主进程？

                        val appEntities = AppEntityModule.provideOtherProcessAppEntityDao(app).queryAll()
                        val appIdSet = appEntities
                            .map { it.appId }
                            .toSet()
                        log("开启的app列表:$appIdSet")
                        if (appIdSet.contains(lpparam.packageName)) {
                            log("开始hook:${lpparam.packageName}")
                            onApplicationCreate(app)

                        }
                    }


                }
            })
        } catch (t: Throwable) {
           log("Hook Application onCreate 失败")
           log(t)
        }


    }

    private fun getContext(lpparam: XC_LoadPackage.LoadPackageParam) {

        try {
            val contextClass = findClass("android.content.ContextWrapper", lpparam.classLoader)
            findAndHookMethod(contextClass, "getApplicationContext", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    param?.apply {
                       log("CSDN_LQR-->得到上下文")

                        val appEntities = AppEntityModule.provideOtherProcessAppEntityDao(result as Context).queryAll()
                        val appIdSet = appEntities
                            .map { it.appId }
                            .toSet()
                       log("开启的app列表:$appIdSet")
                        if (appIdSet.contains(lpparam.packageName)) {
                           log("开始hook:${lpparam.packageName}")
                            onApplicationCreate(result as Context)
                            hookTestApp(lpparam)
                        }
                    }


                }
            });
        } catch (t: Throwable) {
           log("CSDN_LQR-->获取上下文出错");
           log(t)
        }

    }

    private fun onApplicationCreate(context: Context) {
        try {
            UETool.showUETMenu()
        } catch (e: Exception) {
            // Virtual 不支持资源的Hook,这里会挂掉
        }
        //todo 死循环了，好像这里也调用了getApplicationContext方法
        Stetho.initializeWithDefaults(context)
    }

    private fun hookTestApp(lpparam: XC_LoadPackage.LoadPackageParam) {
       log("注册activity hook")
        XposedHelpers.findAndHookMethod(Activity::class.java,
            "onResume",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    val activity = param!!.thisObject as Activity
                    activityHook.onActivityResume(activity)
                }
            })
    }


}