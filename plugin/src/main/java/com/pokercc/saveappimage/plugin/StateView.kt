package com.pokercc.saveappimage.plugin

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView

/**
 * 状态布局
 */
class StateView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 状态
     */
    enum class State {
        /**
         * 默认状态
         */
        NORMAL,
        /**
         * 选择单个view
         */
        SINGLE_VIEW_MODE,
        /**
         * 选择多个view
         */
        MULTI_VIEW_MODE,
        /**
         * 保存全部
         */
        SAVE_ALL_MODE,
    }

    var state: State = State.NORMAL
        set(value) {
            field = value
            changeState(value)
        }


    private fun changeState(state: State) = when (state) {
        State.NORMAL -> {
            textView.text = "保存"

        }
        State.SINGLE_VIEW_MODE -> {
            textView.text = "单个VIEW"

        }
        State.MULTI_VIEW_MODE -> {
            textView.text = "多个VIEW"
        }
        State.SAVE_ALL_MODE -> {
            textView.text = "保存全部"
        }
    }

    private lateinit var textView: TextView
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!this::textView.isInitialized) {
            textView = TextView(context)
            addView(textView)
        }
        state = state

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

    }

}