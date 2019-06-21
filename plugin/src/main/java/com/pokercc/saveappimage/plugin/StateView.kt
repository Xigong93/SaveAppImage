package com.pokercc.saveappimage.plugin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
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

    var onCloseFunctionListener: OnCloseFunctionListener? = null

    private fun changeState(state: State) = when (state) {
        State.NORMAL -> {
            messageTextView.text = "保存"
            button.text = "关闭"
        }
        State.SINGLE_VIEW_MODE -> {
            messageTextView.text = "单个VIEW"
            button.text = "关闭"
        }
        State.MULTI_VIEW_MODE -> {
            messageTextView.text = "多个VIEW"
            button.text = "关闭"

        }
        State.SAVE_ALL_MODE -> {
            messageTextView.text = "保存全部"
            button.text = "关闭"
        }
    }

    private lateinit var messageTextView: TextView
    private lateinit var button: Button
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!this::messageTextView.isInitialized) {
            messageTextView = TextView(context)
            button = Button(context)
            LinearLayout(context)
                .apply {
                    orientation = LinearLayout.HORIZONTAL
                    addView(messageTextView)
                    addView(button)

                }
                .run {
                    this@StateView.addView(this)
                }

            button.setOnClickListener {
                onCloseFunctionListener?.onClose(it)
            }


        }
        state = state

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

    }

    interface OnCloseFunctionListener {
        fun onClose(view: View)
    }

}