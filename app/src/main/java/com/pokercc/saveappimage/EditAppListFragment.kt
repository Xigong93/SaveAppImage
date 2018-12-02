package com.pokercc.saveappimage

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.edit_app_list_fragment.*


/**
 * 编辑app列表
 */
class EditAppListFragment : Fragment() {

    companion object {
        fun newInstance() = EditAppListFragment()
    }

    private lateinit var viewModel: EditAppListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_app_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sureButton.setOnClickListener {
            Toast.makeText(it.context, "确定", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditAppListViewModel::class.java)
        viewModel.appItems.observe(this, Observer {
            it?.apply {
                recyclerView.adapter = EditAppListAdapter(it)

            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadInstallApps()
    }
}

private class EditAppViewHolder(viewGroup: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.edit_app_list_item, viewGroup, false)) {
    val appIcon: ImageView by lazy { itemView.findViewById<ImageView>(R.id.appIcon) }
    val appName: TextView by lazy { itemView.findViewById<TextView>(R.id.appName) }
    val appId: TextView by lazy { itemView.findViewById<TextView>(R.id.appId) }
    val checkBox: CheckBox by lazy { itemView.findViewById<CheckBox>(R.id.checkbox) }


    fun render(appItem: EditAppListViewModel.AppItem) {
        appIcon.setImageDrawable(appItem.appEntity.icon)
        appName.text = appItem.appEntity.name
        appId.text = appItem.appEntity.appId
        checkBox.isChecked = appItem.selected
    }

}

private class EditAppListAdapter(val appEntities: List<EditAppListViewModel.AppItem>) :
    RecyclerView.Adapter<EditAppViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int) = EditAppViewHolder(viewGroup)
    override fun getItemCount() = appEntities.size
    override fun onBindViewHolder(p0: EditAppViewHolder, p1: Int) = p0.render(appEntities[p1])

}