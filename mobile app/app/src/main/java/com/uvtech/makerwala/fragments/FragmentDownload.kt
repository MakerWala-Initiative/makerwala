package com.uvtech.makerwala.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.R
import com.uvtech.makerwala.activities.VideoPlayActivity
import com.uvtech.makerwala.adapters.LocalAdapter
import com.uvtech.makerwala.database.DBHelper
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.helpers.ToastHelper
import com.uvtech.makerwala.interfaces.OnDownloadItemClickListener
import com.uvtech.makerwala.interfaces.OnItemClickListener
import com.uvtech.makerwala.models.DownloadListModel
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.io.File


class FragmentDownload : Fragment(), OnDownloadItemClickListener {

    private lateinit var dbHelper: DBHelper

    private var localListModels = ArrayList<DownloadListModel>()
    private lateinit var localAdapter: LocalAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dbHelper = DBHelper(context!!)
        localAdapter = LocalAdapter(context, localListModels, this)
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.tvTitle.text = resources.getString(R.string.str_download)
        view.toolbar.llLeft.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        dbHelper.getAllVideos()
        if (LoginHelper.instance.isLoggedIn)
            localListModels = dbHelper.getAllVideosUserId(LoginHelper.instance.userData.id)
        localAdapter = LocalAdapter(context, localListModels, this)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = localAdapter
    }

    override fun onItemClick(position: Int) {
        LogHelper.e(localListModels[position].videoFileName)

        val vFile = File(localListModels[position].videoFileName)

        if (vFile.exists()) {
            ApplicationLoader.getInstance().downloadedModel = localListModels[position]
            Utility.callActivity(context!!, VideoPlayActivity::class.java)
        } else {
            ToastHelper.displayInfo("Downloaded File is not exist. Please download it again.")
        }
    }

    override fun onDeleteItemClick(position: Int) {
        ToastHelper.displayDialogOkCancel(context, R.string.str_are_you_sure_to_delete, object : OnItemClickListener {
            override fun onItemClick(posi: Int) {
                DBHelper(context!!).deleteVideo(localListModels[position].id)

                if (localListModels[position].status == 1) {
                    val vFile = File(localListModels[position].videoFileName)
                    val iFile = File(localListModels[position].imageFileName)

                    if (vFile.exists()) vFile.delete()
                    if (iFile.exists()) iFile.delete()
                }

                onResume()
            }
        })
    }

    public fun setAutoStartDownload(){
        for (i in 0 until localListModels.size){
            localListModels[i].autoStarts = 1
        }
        localAdapter.notifyDataSetChanged()
    }

    /*val first =
        "https://r1---sn-bu2a-5hqe.googlevideo.com/videoplayback?expire=1567120986&ei=-QloXbCfJ7SYz7sPq7ixEA&ip=43.242.116.216&id=o-AON407Q4vOV7eUyVrP5QD0hk9qYheS95C5dpCSvEPN4g&itag=18&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-bu2a-5hqe%2Csn-cvh76n7d&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=683750&mime=video%2Fmp4&gir=yes&clen=12363358&ratebypass=yes&dur=284.514&lmt=1552454889561716&mt=1567099306&fvip=1&c=WEB&txp=2201222&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=ALgxI2wwRAIgUY_TY7gPiyTgGA725ICdV3ru620bhLs9WnO8t3OgFBUCIAMyOSOmck9kOWJiKUpnpsGen30_FKab5430QHnwvtVu&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRAIgL6HBcCOvne834j64loet-tuX3gjoDNXTeH8v0P2a3MMCIBpMYcnWgFlXSvvsxyhO-FPbonrjUmQmQiqvcTKHMpKg"
    val image = "https://img.youtube.com/vi/9jBSIVpA4B4/0.jpg"

    val filename = "1567099601473"
    val extension = "mp4"

    private fun start() {

        PRDownloader.download(
            first,
            FileHelper.getRootCacheDirPath(context!!.applicationContext),
            "$filename.$extension"
        )
            .build()
            .setOnStartOrResumeListener { LogHelper.e("setOnStartOrResumeListener") }
            .setOnPauseListener { LogHelper.e("setOnPauseListener") }
            .setOnCancelListener { LogHelper.e("setOnCancelListener") }
            .setOnProgressListener { progress -> LogHelper.e("setOnProgressListener " + progress) }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    LogHelper.e("onDownloadComplete")
                }

                override fun onError(error: Error?) {
                    LogHelper.e("onError " + error?.serverErrorMessage)
                }
            })
    }

    private fun check() {
        val file = File(FileHelper.getRootCacheDirPath(context!!.applicationContext), "$filename.$extension")
        if (file.exists())
            ToastHelper.displayInfo("exist")
        else
            ToastHelper.displayInfo("not exist")
    }

    private fun imageCheck() {}*/
}