package com.pokercc.saveappimage

import android.graphics.drawable.Drawable

/**
 * app数据类
 */
data class AppEntity(
    /**
     *包名
     */
    val appId: String,
    /**
     * 名字
     */
    val name: String,
    /**
     * 图标
     */
    val icon: Drawable

)