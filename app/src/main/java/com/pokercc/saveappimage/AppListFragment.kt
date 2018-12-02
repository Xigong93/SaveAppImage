package com.pokercc.saveappimage

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.app_list_fragment.*


/**
 * app列表
 */
class AppListFragment : Fragment() {

    companion object {
        fun newInstance() = AppListFragment()
    }

    private lateinit var viewModel: AppListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.app_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.appEntities.observe(this, Observer {
            it?.apply {
                editButton.text = if (it.isEmpty()) {
                    "添加"
                } else {
                    "编辑"
                }
                recyclerView.adapter = AppListAdapter(it)


            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AppListViewModel::class.java)

    }

}

private class AppViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.app_list_item, viewGroup, false)) {
    val appIcon: ImageView by lazy { itemView.findViewById<ImageView>(R.id.appIcon) }
    val appName: TextView by lazy { itemView.findViewById<TextView>(R.id.appName) }
    val appId: TextView by lazy { itemView.findViewById<TextView>(R.id.appId) }


    fun render(appEntity: AppWithIcon) {
        appIcon.setImageDrawable(appEntity.icon)
        appName.text = appEntity.appEntity.name
        appId.text = appEntity.appEntity.appId
    }

}

private class AppListAdapter(val appEntities: List<AppWithIcon>) : RecyclerView.Adapter<AppViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int) = AppViewHolder(viewGroup)
    override fun getItemCount() = appEntities.size
    override fun onBindViewHolder(p0: AppViewHolder, p1: Int) = p0.render(appEntities[p1])

}