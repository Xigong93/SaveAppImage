package com.pokercc.saveappimage

import android.graphics.drawable.Drawable
import com.pokercc.saveappimage.database.AppEntity

data class AppWithIcon(
    val appEntity: AppEntity,
    val icon: Drawable
)