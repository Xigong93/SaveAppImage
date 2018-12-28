package com.pokercc.saveappimage.plugin

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout

class FunctionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val button = Button(activity).apply {
            text = "保存drawable"
            tag = BUTTON_TAG
            setPadding(10, 5, 10, 5)
            setBackgroundColor(Color.YELLOW)
            setOnClickListener {
                FunctionDialog(activity).show()
            }
        }

        val frameLayout = FrameLayout(activity)
        frameLayout.addView(button, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).also {
            it.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            it.bottomMargin = 30
        }
        )
        return frameLayout
    }
}