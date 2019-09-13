package com.uvtech.makerwala.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uvtech.makerwala.R
import com.uvtech.makerwala.interfaces.OnItemClickListener
import com.uvtech.makerwala.models.DownloadLinkModel
import kotlinx.android.synthetic.main.row_activity_video_details_download.view.*
import java.text.DecimalFormat

class DownloadLinkAdapter(
    private val mContext: Context?, private val searchListModels: MutableList<DownloadLinkModel>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DownloadLinkAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return searchListModels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext)
                .inflate(R.layout.row_activity_video_details_download, parent, false)
        )
    }

    override fun onBindViewHolder(myViewHolder: DownloadLinkAdapter.MyViewHolder, position: Int) {
        with(getItem(position)) {
            val mb = size.toDouble() / 1024 / 1024
            myViewHolder.tvTitle?.text = mContext!!.resources.getString(
                R.string.str_question_bracket_question, height,
                extension, DecimalFormat("##,##,##,##,##,###.## MB").format(mb).toString()
            )
        }
        myViewHolder.llParent!!.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }

    private fun getItem(position: Int): DownloadLinkModel {
        return searchListModels[position]
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llParent: LinearLayout? = view.llParent
        var tvTitle: TextView? = view.tvTitle
    }
}