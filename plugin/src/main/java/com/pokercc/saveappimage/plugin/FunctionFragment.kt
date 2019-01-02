package com.pokercc.saveappimage.plugin

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable

class FunctionFragment : Fragment() {
    private lateinit var rootView: FrameLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FrameLayout(inflater.context)
            .apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    bottomMargin = 30
//                    padding = 40
//                    paddingLeft = 20
//                    paddingRight = 20
                }
                setPadding(50, 40, 50, 40)
                backgroundDrawable = LightDrawable()
            }.also {
                rootView = it
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StateView(view.context)
            .apply {
                setOnClickListener {
                    FunctionDialog(activity, object : FunctionDialogClickListener {
                        override fun onSingleViewClick(view: View) {
                            this@apply.state = StateView.State.SINGLE_VIEW_MODE


                        }

                        override fun onMultiViewClick(view: View) {
                            this@apply.state = StateView.State.MULTI_VIEW_MODE
                        }

                        override fun onSaveAllViewsClick(view: View) {
                            this@apply.state = StateView.State.SAVE_ALL_MODE
                        }
                    }).show()

                }

                backgroundColor = Color.WHITE
            }
            .also {
                rootView.addView(it)
            }
        setupDrag()
    }

    /**
     * 设置拖拽
     */
    private fun setupDrag() {
        DragHelper(rootView)
//        val point = Point()
//        val dragStartHelper =
//            DragStartHelper(rootView, DragStartHelper.OnDragStartListener { view, dragStartHelper ->
//                dragStartHelper.getTouchPosition(point)
//                view.x = point.x.toFloat()
//                view.y = point.y.toFloat()
//                true
//            })
//        dragStartHelper.attach()
    }

}