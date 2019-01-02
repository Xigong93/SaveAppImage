package pokercc.android.plugindebug

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pokercc.saveappimage.plugin.ActivityHook
import com.pokercc.saveappimage.plugin.DragHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val activityHook = ActivityHook()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupDrag()
    }

    /**
     * 设置拖拽
     */
    fun setupDrag() {
//        val point = Point()
//        val dragStartHelper =
//            DragStartHelper(imageView, DragStartHelper.OnDragStartListener { view, dragStartHelper ->
//                dragStartHelper.getTouchPosition(point)
//                view.x = point.x.toFloat()
//                view.y = point.y.toFloat()
//                true
//            })
//        dragStartHelper.attach()
        DragHelper(imageView)
    }

    override fun onResume() {
        super.onResume()
        activityHook.onActivityResume(this)
    }
}
