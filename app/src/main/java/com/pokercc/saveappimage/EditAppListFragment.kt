package com.pokercc.saveappimage

import android.app.Dialog
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
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
            viewModel.saveAppList()
        }
    }

    var saveDialog: Dialog? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditAppListViewModel::class.java)
        viewModel.appItems.observe(this, Observer {
            it?.apply {
                recyclerView.adapter = EditAppListAdapter(it)

            }
        })
        viewModel.saveAppListState.observe(this, Observer {
            it?.apply {
                saveDialog?.dismiss()
                when (it) {
                    EditAppListViewModel.SaveAppListState.SUCCESS -> {
                        Navigation.findNavController(view!!).popBackStack()
                    }
                    EditAppListViewModel.SaveAppListState.ERROR -> {
                        AlertDialog.Builder(requireContext())
                            .setMessage("保存失败")
                            .setPositiveButton("确定", null)
                            .show()
                    }
                    EditAppListViewModel.SaveAppListState.LOADING -> {
                        saveDialog =
                                ProgressDialog.show(requireContext(), null, "保存中...", true)
                    }
                }
                viewModel.saveAppListState.value = null
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadInstallApps()
    }
}

private class EditAppViewHolder(viewGroup: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(
            R.layout.edit_app_list_item,
            viewGroup,
            false
        )
    ) {
    val appIcon: ImageView by lazy { itemView.findViewById<ImageView>(R.id.appIcon) }
    val appName: TextView by lazy { itemView.findViewById<TextView>(R.id.appName) }
    val appId: TextView by lazy { itemView.findViewById<TextView>(R.id.appId) }
    val checkBox: CheckBox by lazy { itemView.findViewById<CheckBox>(R.id.checkbox) }

    init {
        itemView.setOnClickListener {
            checkBox.performClick()
        }
    }


    fun render(appItem: EditAppListViewModel.AppItem) {
        appIcon.setImageDrawable(appItem.appWithIcon.icon)
        appName.text = appItem.appWithIcon.appEntity.name
        appId.text = appItem.appWithIcon.appEntity.appId
        checkBox.isChecked = appItem.selected
        checkBox.setOnCheckedChangeListener { buttonView, isChecked -> appItem.selected = isChecked }
    }

}

private class EditAppListAdapter(val appEntities: List<EditAppListViewModel.AppItem>) :
    RecyclerView.Adapter<EditAppViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int) = EditAppViewHolder(viewGroup)
    override fun getItemCount() = appEntities.size
    override fun onBindViewHolder(p0: EditAppViewHolder, p1: Int) = p0.render(appEntities[p1])

}