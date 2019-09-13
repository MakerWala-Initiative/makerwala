package com.uvtech.makerwala.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.JsonArray
import com.uvtech.makerwala.R
import com.uvtech.makerwala.models.FilterCategoryModel
import kotlinx.android.synthetic.main.row_activity_filter_category.view.*

class FilterCategoryAdapter(private val mContext: Context?, private val filterCategoryModels: MutableList<FilterCategoryModel>) : RecyclerView.Adapter<FilterCategoryAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return filterCategoryModels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.row_activity_filter_category, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        with(getItem(position)) {
            myViewHolder.tvTitle?.text = title

            myViewHolder.ivCheck?.setImageResource(if (isChecked) R.drawable.ic_action_check else R.drawable.ic_action_uncheck)
        }

        myViewHolder.rlParent!!.setOnClickListener {
            filterCategoryModels[position].isChecked = !filterCategoryModels[position].isChecked
            if (filterCategoryModels[position].id == 0) {
                for (i in 0 until filterCategoryModels.size) {
                    filterCategoryModels[i].isChecked = filterCategoryModels[position].isChecked
                }
            } else {
                if (filterCategoryModels.size > 0) {
                    filterCategoryModels[0].isChecked = false
                }
            }
            notifyDataSetChanged()
        }
    }

    private fun getItem(position: Int): FilterCategoryModel {
        return filterCategoryModels[position]
    }

    fun getSelectedIds(): JsonArray {
        val jsonArray = JsonArray()

        if (filterCategoryModels.size > 0) {
            if (filterCategoryModels[0].isChecked) {
                for (i in 0 until filterCategoryModels.size) {
                    if (i != 0)
                        jsonArray.add(filterCategoryModels[i].id)
                }
            } else {
                for (i in 0 until filterCategoryModels.size) {
                    if (filterCategoryModels[i].isChecked) {
                        jsonArray.add(filterCategoryModels[i].id)
                    }
                }
            }
        }

        return jsonArray
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rlParent: RelativeLayout? = view.rlParent
        var tvTitle: TextView? = view.tvTitle
        var ivCheck: ImageView? = view.ivCheck
    }
}