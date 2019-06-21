package com.pokercc.saveappimage.plugin

import android.app.Activity
import android.view.Window
import com.facebook.stetho.Stetho
import me.ele.uetool.UETool

const val SAVE_APP_IMAGE = "SaveAppImage"

class ActivityHook {

    fun onActivityResume(activity: Activity) {

        val fragment = activity.fragmentManager.findFragmentByTag(SAVE_APP_IMAGE)
        if (fragment == null) {
            activity.fragmentManager.beginTransaction()
                .add(Window.ID_ANDROID_CONTENT, FunctionFragment(), SAVE_APP_IMAGE)
                .commit()

        }
    }
}