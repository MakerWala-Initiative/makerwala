package com.uvtech.makerwala.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.downloader.*
import com.uvtech.makerwala.R
import com.uvtech.makerwala.database.DBHelper
import com.uvtech.makerwala.helpers.FileHelper
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.helpers.ToastHelper
import com.uvtech.makerwala.interfaces.OnDownloadItemClickListener
import com.uvtech.makerwala.models.DownloadListModel
import com.uvtech.makerwala.utilities.EncryptDecrypt
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.row_fragment_local.view.*
import java.io.File


class LocalAdapter(
    private val mContext: Context?, private val searchListModels: ArrayList<DownloadListModel>,
    private val onItemClickListener: OnDownloadItemClickListener
) : RecyclerView.Adapter<LocalAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return searchListModels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.row_fragment_local, parent, false)
        return MyViewHolder(
            view,
            StartDownloadListener(view),
            PauseDownloadListener(view),
            CancelDownloadListener(view),
            ProgressDownloadListener(view),
            DownloadingListener(view)
        )
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        with(searchListModels[position]) {

            try {
                if (status == 0) {// for downloading
                    myViewHolder.cancelDownloadListener.position = position
                    myViewHolder.downloadingListener.position = position

                    myViewHolder.llDownloadParent?.visibility = View.VISIBLE
                    myViewHolder.llVideoParent?.visibility = View.GONE

                    myViewHolder.tvDownloadTitle?.text = name

                    myViewHolder.ibDelete?.setOnClickListener { onItemClickListener.onDeleteItemClick(position) }
                    myViewHolder.ibCancel?.setOnClickListener {
                        if (downloaderStatus != 0)
                            PRDownloader.cancel(downloaderStatus)
                    }
                    myViewHolder.ibPlay?.setOnClickListener {
                        if (Status.RUNNING == PRDownloader.getStatus(downloaderStatus)) {
                            PRDownloader.pause(downloaderStatus)
                        } else {
                            myViewHolder.ibPlay?.isEnabled = false
                            myViewHolder.pbDownloadProgress?.isIndeterminate = true
                            myViewHolder.pbDownloadProgress?.indeterminateDrawable?.setColorFilter(
                                mContext!!.resources.getColor(R.color.colorPrimary),
                                android.graphics.PorterDuff.Mode.SRC_IN
                            )
                            if (Status.PAUSED == PRDownloader.getStatus(downloaderStatus)) {
                                PRDownloader.resume(downloaderStatus)
                            } else {
                                downloaderStatus = PRDownloader.download(
                                    videoUrl,
                                    FileHelper.getRootCacheDirPath(mContext!!.applicationContext),
                                    "$videoFileName.$videoExtension"
                                ).build()
                                    .setOnStartOrResumeListener(myViewHolder.startDownloadListener)
                                    .setOnPauseListener(myViewHolder.pauseDownloadListener)
                                    .setOnCancelListener(myViewHolder.cancelDownloadListener)
                                    .setOnProgressListener(myViewHolder.progressDownloadListener)
                                    .start(myViewHolder.downloadingListener)

                                PRDownloader.download(
                                    imageUrl,
                                    FileHelper.getRootCacheDirPath(mContext.applicationContext),
                                    "$imageFileName.jpg"
                                ).build()
                                    .start(object : OnDownloadListener {
                                        override fun onDownloadComplete() {
                                            val local = searchListModels[position]
                                            local.imageFileName = File(
                                                FileHelper.getRootCacheDirPath(mContext.applicationContext),
                                                "${local.imageFileName}.jpg"
                                            ).absolutePath
                                            DBHelper(mContext).updateLocal(local)
                                        }

                                        override fun onError(error: Error?) {
                                        }
                                    })
                            }
                        }
                    }

                    if (autoStarts == 1) {
                        autoStarts = 0
                        myViewHolder.ibPlay?.performClick()
                    } else {

                    }
                } else {// for downloaded
                    myViewHolder.llDownloadParent?.visibility = View.GONE
                    myViewHolder.llVideoParent?.visibility = View.VISIBLE

                    myViewHolder.tvVideoTitle?.text = name
                    myViewHolder.ibVideoDelete?.setOnClickListener { onItemClickListener.onDeleteItemClick(position) }

                    Glide.with(mContext!!).load(imageFileName)
                        .apply(RequestOptions().error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).centerCrop())
                        .into(myViewHolder.ivVideoImage!!)
                }

            } catch (e: Exception) {
                LogHelper.e(e.message)
            }
        }
        myViewHolder.llVideoParent!!.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }

    fun getItem(position: Int): DownloadListModel? {
        return searchListModels[position]
    }

    inner class StartDownloadListener(val view: View) : OnStartOrResumeListener {
        override fun onStartOrResume() {
            view.pbDownloadProgress.isIndeterminate = false
            view.ibPlay.isEnabled = true
            view.ibPlay.setImageResource(R.drawable.ic_action_pause)
            view.ibCancel.isEnabled = true
            view.ibCancel.setImageResource(R.drawable.ic_action_cancel)
        }
    }

    inner class PauseDownloadListener(val view: View) : OnPauseListener {
        override fun onPause() {
            view.ibPlay.setImageResource(R.drawable.ic_action_play)
        }
    }

    inner class CancelDownloadListener(val view: View) : OnCancelListener {
        var position: Int = 0
        override fun onCancel() {
            searchListModels[position].downloaderStatus = 0
            view.ibPlay.setImageResource(R.drawable.ic_action_play)
            view.ibCancel.isEnabled = false
            view.pbDownloadProgress.progress = 0
            view.tvDownloadProgress.text = ""
            view.pbDownloadProgress.isIndeterminate = false
            notifyDataSetChanged()
        }
    }

    inner class ProgressDownloadListener(val view: View) : OnProgressListener {
        override fun onProgress(progress: Progress?) {
            val progressPercent = progress!!.currentBytes * 100 / progress.totalBytes
            view.pbDownloadProgress.progress = progressPercent.toInt()
            view.tvDownloadProgress.text = Utility.getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
        }
    }

    inner class DownloadingListener(val view: View) : OnDownloadListener {
        var position: Int = 0
        override fun onDownloadComplete() {
            view.ibPlay.isEnabled = false
            view.ibCancel.isEnabled = false
            view.ibPlay.setImageResource(R.drawable.ic_action_play)

            val local = searchListModels[position]

            val filePath = File(
                FileHelper.getRootCacheDirPath(mContext!!.applicationContext),
                "${local.videoFileName}.${local.videoExtension}"
            )

            val encryptFilePath = File(
                FileHelper.getRootCacheDirPath(mContext.applicationContext),
                "${local.videoFileName}encrypt.txt"
            )

            if (encryptFilePath.exists()) encryptFilePath.delete()

            EncryptDecrypt.encode(filePath.absolutePath, encryptFilePath.absolutePath)

            if (filePath.exists()) filePath.delete()

            local.status = 1
            local.downloaderStatus = 1
            local.videoFileName = encryptFilePath.absolutePath
            DBHelper(mContext).updateLocal(local)
            searchListModels[position] = local
            notifyDataSetChanged()
        }

        override fun onError(error: Error?) {
            view.ibPlay.setImageResource(R.drawable.ic_action_play)
            ToastHelper.displayInfo("Some Error Occurred..")
            view.tvDownloadProgress.text = ""
            view.pbDownloadProgress.progress = 0
            searchListModels[position].downloaderStatus = 0
            view.ibCancel.isEnabled = false
            view.pbDownloadProgress.isIndeterminate = false
            view.ibPlay.isEnabled = true
            notifyDataSetChanged()
        }
    }

    inner class MyViewHolder(
        view: View,
        var startDownloadListener: StartDownloadListener,
        var pauseDownloadListener: PauseDownloadListener,
        var cancelDownloadListener: CancelDownloadListener,
        var progressDownloadListener: ProgressDownloadListener,
        var downloadingListener: DownloadingListener
    ) : RecyclerView.ViewHolder(view) {
        var llVideoParent: LinearLayout? = view.llVideoParent
        var ivVideoImage: ImageView? = view.ivVideoImage
        var tvVideoTitle: TextView? = view.tvVideoTitle
        var ibVideoDelete: ImageButton? = view.ibVideoDelete

        var llDownloadParent: LinearLayout? = view.llDownloadParent
        var tvDownloadTitle: TextView? = view.tvDownloadTitle
        var pbDownloadProgress: ProgressBar? = view.pbDownloadProgress
        var tvDownloadProgress: TextView? = view.tvDownloadProgress
        var ibPlay: ImageButton? = view.ibPlay
        var ibCancel: ImageButton? = view.ibCancel
        var ibDelete: ImageButton? = view.ibDelete
    }
}