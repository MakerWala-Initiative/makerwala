package com.uvtech.makerwala.adapters

import android.content.Context
import android.net.UrlQuerySanitizer
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uvtech.makerwala.R
import com.uvtech.makerwala.interfaces.OnItemClickListener
import com.uvtech.makerwala.models.HomeListModel
import kotlinx.android.synthetic.main.row_activity_video_details_related_videos.view.*

class RelatedVideosAdapter(private val mContext: Context?, private val homeListModels: MutableList<HomeListModel>,
                           private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<RelatedVideosAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return homeListModels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedVideosAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.row_activity_video_details_related_videos, parent, false))
    }

    override fun onBindViewHolder(myViewHolder: RelatedVideosAdapter.MyViewHolder, position: Int) {
        with(homeListModels[position]) {
            myViewHolder.tvTitle!!.text = title
            myViewHolder.rbRate!!.rating = rate

            val sanitizer = UrlQuerySanitizer(videoId)
            val s = sanitizer.getValue("v")

            Glide.with(mContext!!).load("https://img.youtube.com/vi/$s/0.jpg")
                    .apply(RequestOptions().error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).centerCrop())
                    .into(myViewHolder.ivImage!!)
        }
        myViewHolder.llParent!!.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }

    fun getItem(position: Int): HomeListModel? {
        return homeListModels[position]
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llParent: LinearLayout? = view.llParent
        var ivImage: ImageView? = view.ivImage
        var tvTitle: TextView? = view.tvTitle
        var rbRate: RatingBar? = view.rbRate
    }
}