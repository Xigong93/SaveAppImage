package com.pokercc.saveappimage.plugin

import android.graphics.PointF
import android.support.v4.view.ViewCompat
import android.view.MotionEvent
import android.view.View

/**
 * view拖动的帮助类
 */
class DragHelper(
    private val target: View
) : View.OnTouchListener {


    /**
     * view原始位置
     */
    private lateinit var originPosition: PointF


    init {
        target.setOnTouchListener(this)
        target.post {
            originPosition = PointF(target.x, target.y)
        }

    }

    private var downPoint: Pair<Int, Int>? = null
    val getViewLocation = { view: View ->
        IntArray(2).apply { view.getLocationOnScreen(this) }
    }
    private val View.rawX
        get() = getViewLocation(this)[0]

    private val View.rawY
        get() = getViewLocation(this)[1]

    override fun onTouch(v: View, event: MotionEvent): Boolean = true.also {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downPoint = event.rawX.toInt() to event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val parent: View = v.parent as View
                downPoint?.apply {
                    var dx = event.rawX - first
                    var dy = event.rawY - second

                    // 不能超出ViewGroup
                    if (v.rawX + dx < parent.rawX) {
                        dx = (parent.rawX - v.rawX).toFloat()
                    } else if (v.rawX + v.width + dx > parent.rawX + parent.width) {
                        dx = (parent.rawX + parent.width - v.rawX - v.width).toFloat()
                    }
                    if (v.rawY + dy < parent.rawY) {
                        dy = (parent.rawY - v.rawY).toFloat()
                    } else if (v.rawY + v.height + dy > parent.rawY + parent.height) {
                        dy = (parent.rawY + parent.height - v.rawY - v.height).toFloat()
                    }


                    ViewCompat.offsetLeftAndRight(v, dx.toInt())
                    ViewCompat.offsetTopAndBottom(v, dy.toInt())
                }
                downPoint = event.rawX.toInt() to event.rawY.toInt()

            }
            else -> {
            }
        }

    }

}