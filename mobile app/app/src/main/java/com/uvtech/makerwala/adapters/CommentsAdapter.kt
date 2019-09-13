package com.uvtech.makerwala.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.finlitetech.livekeeping.helpers.DateHelper
import com.uvtech.makerwala.R
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.interfaces.OnCommentItemClickListener
import com.uvtech.makerwala.models.CommentsModel
import kotlinx.android.synthetic.main.row_activity_video_details_comments.view.*

class CommentsAdapter(
    private val mContext: Context?, private val searchListModels: MutableList<CommentsModel>,
    private val onItemClickListener: OnCommentItemClickListener
) : RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return searchListModels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext)
                .inflate(R.layout.row_activity_video_details_comments, parent, false)
        )
    }

    override fun onBindViewHolder(myViewHolder: CommentsAdapter.MyViewHolder, position: Int) {
        with(searchListModels[position]) {

            if(createdBy == LoginHelper.instance.userData.id){
                myViewHolder.ivDelete?.visibility = View.VISIBLE
                myViewHolder.ivEdit?.visibility = View.VISIBLE
            }else{
                myViewHolder.ivDelete?.visibility = View.GONE
                myViewHolder.ivEdit?.visibility = View.GONE
            }

            try {
                Glide.with(mContext!!).load(imageProfile)
                    .apply(
                        RequestOptions().error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).transform(
                            CircleCrop()
                        )
                    )
                    .into(myViewHolder.ivImage!!)
                myViewHolder.tvUserName!!.text = userName
                myViewHolder.tvComment!!.text = comments
                myViewHolder.tvTime!!.text = DateHelper.getFormattedDate(
                    DateHelper.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_ZZZ, DateHelper.DATE_FORMAT_DDMMMYY, time
                )
                myViewHolder.rbRate!!.rating = rating.toFloat()

            } catch (e: Exception) {
                LogHelper.e(e.message)
            }
        }
        myViewHolder.llParent!!.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
        myViewHolder.ivEdit!!.setOnClickListener {
            onItemClickListener.onEditItemClick(position)
        }
        myViewHolder.ivDelete!!.setOnClickListener {
            onItemClickListener.onDeleteItemClick(position)
        }
    }

    fun getItem(position: Int): CommentsModel? {
        return searchListModels[position]
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llParent: LinearLayout? = view.llParent
        var ivImage: ImageView? = view.ivImage
        var tvUserName: TextView? = view.tvUserName
        var tvTime: TextView? = view.tvTime
        var tvComment: TextView? = view.tvComment
        var rbRate: RatingBar? = view.rbRate
        var ivEdit: ImageView? = view.ivEdit
        var ivDelete: ImageView? = view.ivDelete
    }
}