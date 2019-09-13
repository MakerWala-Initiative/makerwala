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
import com.uvtech.makerwala.viewholder.PaginationViewHolder
import kotlinx.android.synthetic.main.row_fragment_home.view.*

class HomeListAdapter(private val mContext: Context?, private val homeListModels: ArrayList<HomeListModel>,
                      private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoadingAdded = false

    val isEmpty: Boolean
        get() = itemCount == 0

    override fun getItemCount(): Int {
        return homeListModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == homeListModels.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val itemView = LayoutInflater.from(mContext)
                        .inflate(R.layout.row_fragment_home, parent, false)

                viewHolder = MyViewHolder(itemView)
            }
            LOADING -> {
                val v2 = inflater.inflate(R.layout.row_pagination_progress_bar, parent, false)
                viewHolder = PaginationViewHolder(v2)
            }
            else -> {
                val v2 = inflater.inflate(R.layout.row_pagination_progress_bar, parent, false)
                viewHolder = PaginationViewHolder(v2)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            LOADING -> {
            }
            ITEM -> {
                val myViewHolder = holder as MyViewHolder
                with(homeListModels[position]){
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
        }
    }

    fun add(mc: HomeListModel) {
        homeListModels.add(mc)
        notifyItemInserted(homeListModels.size - 1)
    }

    fun addAll(mcList: List<HomeListModel>) {
        for (mc in mcList) {
            add(mc)
        }
    }

    private fun remove(city: HomeListModel?) {
        val position = homeListModels.indexOf(city)
        if (position > -1) {
            homeListModels.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun addLoadingFooter() {
        if (!isLoadingAdded) {
            isLoadingAdded = true
            add(HomeListModel())
        }
    }

    fun removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false

            if (homeListModels.size > 0) {
                val position = homeListModels.size - 1
                val item = getItem(position)

                if (item != null) {
                    homeListModels.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    private fun getItem(position: Int): HomeListModel? {
        return homeListModels[position]
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llParent: LinearLayout? = view.llParent
        var ivImage: ImageView? = view.ivImage
        var tvTitle: TextView? = view.tvTitle
        var rbRate: RatingBar? = view.rbRate
    }

    companion object {

        private val ITEM = 0
        private val LOADING = 1
    }
}