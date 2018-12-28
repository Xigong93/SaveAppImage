package pokercc.android.plugindebug

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.pokercc.saveappimage.plugin.ActivityHook

class MainActivity : AppCompatActivity() {

    private val activityHook = ActivityHook()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        activityHook.onActivityResume(this)
    }
}
