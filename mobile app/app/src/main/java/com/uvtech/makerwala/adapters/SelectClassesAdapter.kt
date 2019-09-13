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
import com.uvtech.makerwala.models.CountryModel
import kotlinx.android.synthetic.main.row_activity_register_select_classes.view.*
import org.json.JSONArray

/**
 * Created by devoguru on 24/02/18.
 */
class SelectClassesAdapter(private val mContext: Context, private val companyTempModels: MutableList<CountryModel>)
    : RecyclerView.Adapter<SelectClassesAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return companyTempModels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.row_activity_register_select_classes, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        with(getItem(position)) {
            myViewHolder.tvTitle?.text = title

            myViewHolder.ivCheck?.setImageResource(if (isChecked) R.drawable.ic_action_check else R.drawable.ic_action_uncheck)
        }

        myViewHolder.rlParent!!.setOnClickListener {
            companyTempModels[position].isChecked = !companyTempModels[position].isChecked
            notifyDataSetChanged()
        }
    }

    private fun getItem(position: Int): CountryModel {
        return companyTempModels[position]
    }

    fun getSelectedIds(): JsonArray {
        val jsonArray = JsonArray()

        for (i in 0 until companyTempModels.size) {
            if (companyTempModels[i].isChecked) {
                jsonArray.add(companyTempModels[i].id)
            }
        }

        return jsonArray
    }

    fun getIds(): JSONArray {
        val jsonArray = JSONArray ()

        for (i in 0 until companyTempModels.size) {
            if (companyTempModels[i].isChecked) {
                jsonArray.put(companyTempModels[i].id)
            }
        }

        return jsonArray
    }

    fun getSelectedNames(): String {
        val stringBuffer = StringBuffer()

        for (i in 0 until companyTempModels.size) {
            if (companyTempModels[i].isChecked) {
                if (stringBuffer.toString().isNotEmpty())
                    stringBuffer.append(", ")
                stringBuffer.append(companyTempModels[i].title)
            }
        }

        return stringBuffer.toString()
    }

    fun getNames(): JSONArray {
        val jsonArray = JSONArray ()

        for (i in 0 until companyTempModels.size) {
            if (companyTempModels[i].isChecked) {
                jsonArray.put(companyTempModels[i].title)
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