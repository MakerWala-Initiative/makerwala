package com.uvtech.makerwala.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.UrlQuerySanitizer
import android.os.AsyncTask
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.gson.JsonObject
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.ApplicationConstants.REQUEST_PERMISSIONS_STORAGE
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.BuildConfig
import com.uvtech.makerwala.R
import com.uvtech.makerwala.adapters.CommentsAdapter
import com.uvtech.makerwala.adapters.DownloadLinkAdapter
import com.uvtech.makerwala.adapters.RelatedVideosAdapter
import com.uvtech.makerwala.api.*
import com.uvtech.makerwala.database.DBHelper
import com.uvtech.makerwala.helpers.JsonHelper
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.helpers.ToastHelper
import com.uvtech.makerwala.interfaces.OnCommentItemClickListener
import com.uvtech.makerwala.interfaces.OnItemClickListener
import com.uvtech.makerwala.models.*
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_video_details.*
import kotlinx.android.synthetic.main.dialog_edit_comment.view.*
import kotlinx.android.synthetic.main.dialog_list.view.*
import kotlinx.android.synthetic.main.dialog_title.view.*
import org.json.JSONObject
import java.net.URL

class VideoDetailsActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    private var relatedVideosListModels = ArrayList<HomeListModel>()
    private lateinit var relatedVideosListAdapter: RelatedVideosAdapter

    private var commentsModels = ArrayList<CommentsModel>()
    private lateinit var commentsAdapter: CommentsAdapter

    private var mYoutubeDataApi: YouTube? = null
    private var youTubePlayer: YouTubePlayer? = null

    private var downloadLinkModels = ArrayList<DownloadLinkModel>()

    private lateinit var homeListModel: HomeListModel
    private var videoId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_details)

        refreshScreen()
    }

    private fun refreshScreen() {

        mYoutubeDataApi = YouTube.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory(), null)
            .setApplicationName(resources.getString(R.string.app_name))
            .build()

        homeListModel = ApplicationLoader.getInstance().homeListModel

        init()

        ivExpandDetails.setOnClickListener { etvDetails.expandText() }
        ivDownload.setOnClickListener {
            if (LoginHelper.instance.isLoggedIn) {
                onClickDownload()
            } else {
                ToastHelper.displayInfo(R.string.str_login_required)
            }
        }
        ivComment.setOnClickListener {
            if (LoginHelper.instance.isLoggedIn) {
                llCommentSection.visibility =
                    if (llCommentSection.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            } else {
                ToastHelper.displayInfo(R.string.str_login_required)
            }
        }

        ivShare.setOnClickListener { onClickShare() }
        btnAdd.setOnClickListener { onClickAddComment() }

        callVideoDetails()
        callTransactionLog()
        ApplicationLoader.getInstance().callActivityLog(WebFields.INFORMATION, "Video Details")
    }

    private fun init() {
        relatedVideosListAdapter = RelatedVideosAdapter(this, relatedVideosListModels, onRelatedVideosClickListener)
        recyclerViewRelatedVideo!!.layoutManager = LinearLayoutManager(this)
        recyclerViewRelatedVideo.isNestedScrollingEnabled = false
        recyclerViewRelatedVideo!!.adapter = relatedVideosListAdapter

        commentsAdapter = CommentsAdapter(this, commentsModels, onCommentsClickListener)
        recyclerViewComments!!.layoutManager = LinearLayoutManager(this)
        recyclerViewComments.isNestedScrollingEnabled = false
        recyclerViewComments!!.adapter = commentsAdapter

        val commentTypeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, commentTypeNames
        )
        commentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCommentTypes.adapter = commentTypeAdapter
        spinnerCommentTypes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                commentTypeId = commentTypeModels[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                LogHelper.e("item nothing ")
            }
        }
        callGetType()

        val youTubePlayerFragment = fragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
        youTubePlayerFragment.initialize(
            ApplicationConstants.YOUTUBE_API,
            this
        )

        val stringBuilder = StringBuilder()
        stringBuilder.append(homeListModel.publicDetail)
        stringBuilder.append(" ")
        stringBuilder.append(homeListModel.privateDetail)
        etvDetails.text = stringBuilder.toString()
        tvTitle!!.text = homeListModel.title
        rbRating!!.rating = homeListModel.rate

        Glide.with(this).load("https://img.youtube.com/vi/$videoId/0.jpg")
            .apply(RequestOptions().error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).circleCrop())
            .into(ivProfile!!)
        try {
            val sanitizer = UrlQuerySanitizer(homeListModel.videoId)
            videoId = sanitizer.getValue("v")
            llInvalidYoutubeLink.visibility = View.GONE
        } catch (e: Exception) {
            ApplicationLoader.getInstance().callActivityLog(WebFields.EXCEPTION, "Video Details - " + e.message)
            videoId = ""
            llInvalidYoutubeLink.visibility = View.VISIBLE
            LogHelper.e(e.message)
        }

        if (videoId.isNotEmpty())
            extractVideo()

        if (videoId.isNotEmpty()) {
            youTubePlayer?.cueVideo(videoId) // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
            youTubePlayer?.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                override fun onLoading() {

                }

                override fun onLoaded(s: String) {
                    youTubePlayer!!.play()
                }

                override fun onAdStarted() {

                }

                override fun onVideoStarted() {

                }

                override fun onVideoEnded() {

                }

                override fun onError(errorReason: YouTubePlayer.ErrorReason) {

                }
            })
        }
    }

    private val onRelatedVideosClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            ApplicationLoader.getInstance().homeListModel = relatedVideosListModels[position]
            refreshScreen()
        }
    }

    private val onCommentsClickListener = object : OnCommentItemClickListener {
        override fun onItemClick(position: Int) {

        }

        override fun onDeleteItemClick(pos: Int) {
            ToastHelper.displayDialogOkCancel(this@VideoDetailsActivity, R.string.str_are_you_sure_to_delete,
                object : OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val jsonObjectParam = JsonObject()
                        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
                        jsonObjectParam.addProperty(
                            WebFields.USERNAME,
                            if (LoginHelper.instance.userData.teacherEmailAddress.isEmpty()) ApplicationConstants.API_DEFAULT_USERNAME else LoginHelper.instance.userData.teacherEmailAddress
                        )
                        jsonObjectParam.addProperty(
                            WebFields.PASSWORD,
                            if (LoginHelper.instance.userData.password.isEmpty()) ApplicationConstants.API_DEFAULT_PASSWORD else LoginHelper.instance.userData.password
                        )
                        jsonObjectParam.addProperty(WebFields.TRANS_ID,commentsModels[pos].transId)
                        jsonObjectParam.addProperty(WebFields.IS_RECORD_DELETE,1)

                        ApiCallingHelper().callPostApi(
                            this@VideoDetailsActivity, WebLinks.GET_TRANSACTION_LOG, jsonObjectParam,
                            object : ApiCallingInterface {
                                override fun onSuccess(response: String, message: String) {
                                    commentsModels.removeAt(pos)
                                    commentsAdapter.notifyDataSetChanged()
                                }

                                override fun onFailure(errorMessage: String) {
                                    ToastHelper.displayInfo(errorMessage)
                                }
                            }
                        )
                    }
                })
        }

        override fun onEditItemClick(position: Int) {
            openDialogEditComment(position)
        }
    }

    private fun extractVideo() {
        downloadLinkModels.clear()

        myYoutubeExtractor = MyYoutubeExtractor(this)
        myYoutubeExtractor.setDownloadLinkModels(downloadLinkModels)
        myYoutubeExtractor.extract("https://youtube.com/watch?v=$videoId",true,false)
//        myYoutubeExtractor.execute(
//            "https://youtube.com/watch?v=$videoId"
//        )
    }

    private lateinit var myYoutubeExtractor: MyYoutubeExtractor

    class MyYoutubeExtractor(context: Context) :
        YouTubeExtractor(context) {
        private var downloadLinkModels = ArrayList<DownloadLinkModel>()

        fun setDownloadLinkModels(downloadLinkModels: ArrayList<DownloadLinkModel>) {
            this.downloadLinkModels = downloadLinkModels
        }

        override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
            if (ytFiles != null) {
                for (i in 0 until ytFiles.size()) {

//                    LogHelper.e(ytFiles.get(ytFiles.keyAt(i)).format.audioCodec.toString())
//                    LogHelper.e(ytFiles.get(ytFiles.keyAt(i)).format.videoCodec.toString())
//                    LogHelper.e("....")
                    if (ytFiles.get(ytFiles.keyAt(i)).format.ext.equals("mp4", ignoreCase = true)
                        && ytFiles.get(ytFiles.keyAt(i)).format.audioBitrate != -1
                        && ytFiles.get(ytFiles.keyAt(i)).format.height != -1
                    ) {
                        LogHelper.e(
                            "ext " + ytFiles.get(ytFiles.keyAt(i)).format.ext + "," +
                                    "height " + ytFiles.get(ytFiles.keyAt(i)).format.height.toString() + "," +
                                    "fps " + ytFiles.get(ytFiles.keyAt(i)).format.fps.toString() + "," +
                                    "audiobitrate " + ytFiles.get(ytFiles.keyAt(i)).format.audioBitrate.toString() + "," +
                                    "itag " + ytFiles.get(ytFiles.keyAt(i)).format.itag.toString() + "," +
                                    "ishls " + ytFiles.get(ytFiles.keyAt(i)).format.isHlsContent.toString() + "," +
                                    "isdash " + ytFiles.get(ytFiles.keyAt(i)).format.isDashContainer.toString()
                        )

                        val downloadLinkModel = DownloadLinkModel(ytFiles.keyAt(i))
                        downloadLinkModel.link = ytFiles.get(ytFiles.keyAt(i)).url
                        downloadLinkModel.height = ytFiles.get(ytFiles.keyAt(i)).format.height.toString()
                        downloadLinkModel.extension = ytFiles.get(ytFiles.keyAt(i)).format.ext
                        downloadLinkModels.add(downloadLinkModel)
                    }
                }

                FetchTask(downloadLinkModels).execute()
            }
        }
    }

    class FetchTask(var downloadLinkModels: ArrayList<DownloadLinkModel>) : AsyncTask<Void, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): String {
            try {
                for (i in 0 until downloadLinkModels.size) {
                    val url = URL(downloadLinkModels[i].link)
                    val urlConnection = url.openConnection()
                    urlConnection.connect()
                    val fileSize = urlConnection.contentLength
                    downloadLinkModels[i].size = fileSize
                    downloadLinkModels[i].isNotAvailable = true
                }
                return "done"
            } catch (e: Exception) {
                LogHelper.e(e.message)
            }

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            LogHelper.e("fetch complete")

            val array = ArrayList<Int>()
            for (i in 0 until downloadLinkModels.size) {
                LogHelper.e(downloadLinkModels[i].toString())
                if (downloadLinkModels[i].size == 0)
                    array.add(i)
            }

            for (i in array.size - 1 downTo 0) {
                downloadLinkModels.removeAt(array[i])
            }
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
//        player?.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT
//        player?.setOnFullscreenListener(OnFullscreenListener {
//        })
        if (!wasRestored) {
            if (videoId.isNotEmpty()) {
                player?.cueVideo(videoId) // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                player?.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                    override fun onLoading() {

                    }

                    override fun onLoaded(s: String) {
                        youTubePlayer = player
                        player.play()
                    }

                    override fun onAdStarted() {

                    }

                    override fun onVideoStarted() {

                    }

                    override fun onVideoEnded() {

                    }

                    override fun onError(errorReason: YouTubePlayer.ErrorReason) {

                    }
                })
            }
        }
    }

    private lateinit var downloadLinkAdapter: DownloadLinkAdapter

    private lateinit var dialogDownload: AlertDialog
    private lateinit var downloadModel: DownloadListModel

    private fun onClickDownload() {
        val builder = AlertDialog.Builder(this@VideoDetailsActivity)
        val dialogTitle = View.inflate(this, R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(R.string.str_download)
        builder.setCustomTitle(dialogTitle)

        val dialogView = View.inflate(this, R.layout.dialog_list, null)

        downloadLinkAdapter = DownloadLinkAdapter(this,
            downloadLinkModels, object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    dialogDownload.dismiss()
                    downloadModel = DownloadListModel(
                        0, LoginHelper.instance.userData.id, homeListModel.title, 0,
                        downloadLinkModels[position].link,
                        downloadLinkModels[position].extension,
                        "https://img.youtube.com/vi/$videoId/0.jpg",
                        System.currentTimeMillis().toString() + "video",
                        System.currentTimeMillis().toString() + "image"
                    )
//                    DBHelper(this@VideoDetailsActivity)
//                        .addVideo()
//                    downloadUrl = downloadLinkModels[position].link
//                    downloadExt = downloadLinkModels[position].extension
//
                    checkStorePermission()
                }
            })
        dialogView.recyclerView!!.layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(resources.getDrawable(R.drawable.list_bar))
        dialogView.recyclerView.addItemDecoration(itemDecor)
        dialogView.recyclerView!!.adapter = downloadLinkAdapter

        builder.setView(dialogView)
        dialogDownload = builder.create()
        dialogDownload.show()
    }

    private fun checkStorePermission() {
        if (checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS_STORAGE
            )
            return
        }
        addedToDownload()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_STORAGE)
            addedToDownload()
    }

    private fun addedToDownload() {
        LogHelper.e(downloadModel.toString())
        DBHelper(this@VideoDetailsActivity)
            .addVideo(downloadModel)
        setResult(Activity.RESULT_OK)
        onBackPressed()
    }

    private fun callVideoDetails() {
        pbLoading.visibility = View.VISIBLE
        nsvDetails.visibility = View.GONE

        val jsonObjectParam = JsonObject()
        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
        jsonObjectParam.addProperty(
            WebFields.USERNAME,
            if (LoginHelper.instance.userData.teacherEmailAddress.isEmpty()) ApplicationConstants.API_DEFAULT_USERNAME else LoginHelper.instance.userData.teacherEmailAddress
        )
        jsonObjectParam.addProperty(
            WebFields.PASSWORD,
            if (LoginHelper.instance.userData.password.isEmpty()) ApplicationConstants.API_DEFAULT_PASSWORD else LoginHelper.instance.userData.password
        )
        jsonObjectParam.addProperty(WebFields.VIDEO_ID, homeListModel.id)

        ApiCallingHelper().callPostApi(
            this, WebLinks.GET_VIDEO_DETAIL, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    try {
                        pbLoading.visibility = View.GONE
                        nsvDetails.visibility = View.VISIBLE
                        var jsonObject = JSONObject(response)
                        jsonObject = jsonObject.getJSONObject(WebFields.DATA)

                        val jsonArrayRelated = jsonObject.getJSONArray(WebFields.RELATIVE_VIDEO_LIST)
                        relatedVideosListModels.clear()
                        for (i in 0 until jsonArrayRelated.length()) {
                            val json = jsonArrayRelated.getJSONObject(i)
                            val s = HomeListModel(
                                JsonHelper.getInt(json, WebFields.VIDEO_ID),
                                JsonHelper.getString(json, WebFields.YOUTUBE_LINK),
                                JsonHelper.getString(json, WebFields.VIDEO_TITLE),
                                JsonHelper.getString(json, WebFields.PUBLIC_DETAIL),
                                JsonHelper.getString(json, WebFields.PRIVATE_DETAIL),
                                JsonHelper.getDouble(json, WebFields.RATING).toFloat(),
                                JsonHelper.getBoolean(json, WebFields.IS_PRIVATE)
                            )

                            relatedVideosListModels.add(s)
                        }

                        relatedVideosListAdapter.notifyDataSetChanged()

                        val jsonArrayLog = jsonObject.getJSONArray(WebFields.TRANSACTION_LOG)
                        commentsModels.clear()
                        for (i in 0 until jsonArrayLog.length()) {
                            val json = jsonArrayLog.getJSONObject(i)
                            val c = CommentsModel(
                                JsonHelper.getString(json, WebFields.COMMENT_TEXT),
                                JsonHelper.getString(json, WebFields.USERNAME),
                                JsonHelper.getString(json, WebFields.IMG_PROFILE),
                                JsonHelper.getString(json, WebFields.CREATED_DATE),
                                JsonHelper.getString(json, WebFields.RATING),
                                JsonHelper.getInt(json, WebFields.CREATED_BY),
                                JsonHelper.getInt(json, WebFields.TRANS_ID),
                                JsonHelper.getInt(json, WebFields.TYPE_ID)
                            )

                            commentsModels.add(c)
                        }

                        commentsAdapter =
                            CommentsAdapter(this@VideoDetailsActivity, commentsModels, onCommentsClickListener)
                        recyclerViewComments!!.layoutManager = LinearLayoutManager(this@VideoDetailsActivity)
                        recyclerViewComments.isNestedScrollingEnabled = false
                        recyclerViewComments!!.adapter = commentsAdapter
                    }catch (e:Exception){
                        ApplicationLoader.getInstance().callActivityLog(WebFields.EXCEPTION, "Video Details - " + e.message)
                    }
                }

                override fun onFailure(errorMessage: String) {
                    pbLoading.visibility = View.GONE
                    nsvDetails.visibility = View.VISIBLE
                    ToastHelper.displayInfo(errorMessage)
                }
            }, false
        )
    }

    var commentTypeId = 0
    var commentTypeNames = arrayOf<String?>()
    var commentTypeModels = ArrayList<CountryModel>()

    private fun callGetType() {
        commentTypeNames = arrayOf("")
        commentsModels = ArrayList()
        val jsonObjectParam = JsonObject()
        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
        jsonObjectParam.addProperty(
            WebFields.USERNAME,
            if (LoginHelper.instance.userData.teacherEmailAddress.isEmpty()) ApplicationConstants.API_DEFAULT_USERNAME else LoginHelper.instance.userData.teacherEmailAddress
        )
        jsonObjectParam.addProperty(
            WebFields.PASSWORD,
            if (LoginHelper.instance.userData.password.isEmpty()) ApplicationConstants.API_DEFAULT_PASSWORD else LoginHelper.instance.userData.password
        )

        ApiCallingHelper().callPostApi(
            this, WebLinks.GET_TYPE, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val jsonObject = JSONObject(response)
                    val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                    commentTypeNames = arrayOfNulls(jsonArrayData.length())
                    for (i in 0 until jsonArrayData.length()) {
                        val json = jsonArrayData.getJSONObject(i)
                        commentTypeNames[i] = json.getString(WebFields.TYPE_NAME)
                        commentTypeModels.add(
                            CountryModel(
                                JsonHelper.getInt(json, WebFields.TYPE_ID),
                                JsonHelper.getString(json, WebFields.TYPE_NAME)
                            )
                        )
                    }

                    if (commentTypeModels.size > 0) {
                        commentTypeId = commentTypeModels[0].id
                    }

                    val commentTypeAdapter = ArrayAdapter(
                        this@VideoDetailsActivity,
                        android.R.layout.simple_spinner_item, commentTypeNames
                    )
                    commentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCommentTypes.adapter = commentTypeAdapter
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            }, false
        )
    }

    private fun checkCommentValidation(): Boolean {
        if (etComment.text.toString().isEmpty()) {
            ToastHelper.displayInfo(R.string.str_add_comment)
            return false
        }
        return true
    }

    private fun onClickAddComment() {
        if (checkCommentValidation()) {
            Utility.hideKeyboard(this, btnAdd)
            val jsonObjectParam = JsonObject()
            jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
            jsonObjectParam.addProperty(
                WebFields.USERNAME,
                if (LoginHelper.instance.userData.teacherEmailAddress.isEmpty()) ApplicationConstants.API_DEFAULT_USERNAME else LoginHelper.instance.userData.teacherEmailAddress
            )
            jsonObjectParam.addProperty(
                WebFields.PASSWORD,
                if (LoginHelper.instance.userData.password.isEmpty()) ApplicationConstants.API_DEFAULT_PASSWORD else LoginHelper.instance.userData.password
            )
            jsonObjectParam.addProperty(WebFields.TRANS_TYPE_ID, "1")
            jsonObjectParam.addProperty(WebFields.TYPE_ID, commentTypeId.toString())
            jsonObjectParam.addProperty(WebFields.COMMENT_TEXT, etComment.text.toString())
            jsonObjectParam.addProperty(WebFields.RATING, rbCommentRate.rating.toInt().toString())
            jsonObjectParam.addProperty(WebFields.REF_ID, homeListModel.id.toString())
            jsonObjectParam.addProperty(WebFields.REF_TYPE_ID, "1")
            jsonObjectParam.addProperty(
                WebFields.CREATED_BY,
                if (LoginHelper.instance.userData.id == 0) ApplicationConstants.API_DEFAULT_USERID else LoginHelper.instance.userData.id.toString()
            )
            jsonObjectParam.addProperty(WebFields.IS_EDITED, "0")
            jsonObjectParam.addProperty(WebFields.EDITED_BY, "0")
            jsonObjectParam.addProperty(WebFields.IS_DELETED, "0")
            jsonObjectParam.addProperty(WebFields.DELETED_BY, "0")

            ApiCallingHelper().callPostApi(
                this, WebLinks.GET_TRANSACTION_LOG, jsonObjectParam,
                object : ApiCallingInterface {
                    override fun onSuccess(response: String, message: String) {
                        var jsonObject = JSONObject(response)
                        jsonObject = jsonObject.getJSONObject(WebFields.DATA)
                        etComment.setText("")
                        rbCommentRate.rating = 0f
                        val c = CommentsModel(
                            JsonHelper.getString(jsonObject, WebFields.COMMENT_TEXT),
                            JsonHelper.getString(jsonObject, WebFields.USERNAME),
                            JsonHelper.getString(jsonObject, WebFields.IMG_PROFILE),
                            JsonHelper.getString(jsonObject, WebFields.CREATED_DATE),
                            JsonHelper.getString(jsonObject, WebFields.RATING),
                            JsonHelper.getInt(jsonObject, WebFields.CREATED_BY)
                        )

                        commentsModels.add(c)

                        commentsAdapter =
                            CommentsAdapter(this@VideoDetailsActivity, commentsModels, onCommentsClickListener)
                        recyclerViewComments!!.layoutManager = LinearLayoutManager(this@VideoDetailsActivity)
                        recyclerViewComments.isNestedScrollingEnabled = false
                        recyclerViewComments!!.adapter = commentsAdapter
                    }

                    override fun onFailure(errorMessage: String) {
                        ToastHelper.displayInfo(errorMessage)
                    }
                }, false
            )
        }
    }

    private fun callTransactionLog() {
        val jsonObjectParam = JsonObject()
        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
        jsonObjectParam.addProperty(
            WebFields.USERNAME,
            if (LoginHelper.instance.userData.teacherEmailAddress.isEmpty()) ApplicationConstants.API_DEFAULT_USERNAME else LoginHelper.instance.userData.teacherEmailAddress
        )
        jsonObjectParam.addProperty(
            WebFields.PASSWORD,
            if (LoginHelper.instance.userData.password.isEmpty()) ApplicationConstants.API_DEFAULT_PASSWORD else LoginHelper.instance.userData.password
        )
        jsonObjectParam.addProperty(WebFields.TRANS_TYPE_ID, "3")
        jsonObjectParam.addProperty(WebFields.TYPE_ID, "0")
        jsonObjectParam.addProperty(WebFields.COMMENT_TEXT, "")
        jsonObjectParam.addProperty(WebFields.RATING, "")
        jsonObjectParam.addProperty(WebFields.REF_ID, homeListModel.id.toString())
        jsonObjectParam.addProperty(WebFields.REF_TYPE_ID, "1")
        jsonObjectParam.addProperty(
            WebFields.CREATED_BY,
            if (LoginHelper.instance.userData.id == 0) ApplicationConstants.API_DEFAULT_USERID else LoginHelper.instance.userData.id.toString()
        )
        jsonObjectParam.addProperty(WebFields.IS_EDITED, "0")
        jsonObjectParam.addProperty(WebFields.EDITED_BY, "0")
        jsonObjectParam.addProperty(WebFields.IS_DELETED, "0")
        jsonObjectParam.addProperty(WebFields.DELETED_BY, "0")

        ApiCallingHelper().callPostApi(
            this, WebLinks.GET_TRANSACTION_LOG, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
//                    val jsonObject = JSONObject(response)

                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            }, false
        )
    }

    private fun onClickShare() {
        val builder = Uri.Builder()
        builder.scheme(getString(R.string.config_scheme))
            .authority(getString(R.string.config_host))
            .appendQueryParameter(WebFields.VIDEO_ID, homeListModel.id.toString())
            .appendQueryParameter(WebFields.YOUTUBE_LINK, homeListModel.videoId)
            .appendQueryParameter(WebFields.VIDEO_TITLE, homeListModel.title)
            .appendQueryParameter(WebFields.PUBLIC_DETAIL, homeListModel.publicDetail)
            .appendQueryParameter(WebFields.PRIVATE_DETAIL, homeListModel.privateDetail)
            .appendQueryParameter(WebFields.RATING, homeListModel.rate.toString())
            .appendQueryParameter(WebFields.IS_PRIVATE, homeListModel.isPrivate.toString())
        val uri = builder.build()

        ivShare.isEnabled = false
        LoadingHelper.getInstance().show(this)
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(uri)
            .setDynamicLinkDomain("makerwala.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(packageName)
                    .setMinimumVersion(BuildConfig.VERSION_CODE)
                    .build()
            )
//                    .setIosParameters(
//                            DynamicLink.IosParameters.Builder("com.Finlitetech.LiveKeeping")
//                                    .setAppStoreId("1451277319")
//                                    .setMinimumVersion("1.0.10")
//                                    .build())
            .setNavigationInfoParameters(
                DynamicLink.NavigationInfoParameters.Builder()
                    .setForcedRedirectEnabled(true).build()
            )
            .buildShortDynamicLink()
            .addOnSuccessListener { shortDynamicLink ->
                LoadingHelper.getInstance().dismiss()
                val mInvitationUrl = shortDynamicLink.shortLink
                LogHelper.e(mInvitationUrl.toString())
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "$mInvitationUrl")
                startActivity(Intent.createChooser(shareIntent, "choose one"))
                ivShare.isEnabled = true
            }
            .addOnFailureListener {
                LoadingHelper.getInstance().dismiss()
                ivShare.isEnabled = true
                LogHelper.e(it.message)
            }
    }

    private lateinit var dialogEditComment: AlertDialog

    private fun openDialogEditComment(position: Int) {
        val builder = AlertDialog.Builder(this)
        val dialogTitle = View.inflate(this, R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(R.string.str_edit_comment)
        builder.setCustomTitle(dialogTitle)

        val dialogView = View.inflate(this, R.layout.dialog_edit_comment, null)

        dialogView.rbCommentRate.rating = commentsModels[position].rating.toFloat()
        dialogView.etComment.setText(commentsModels[position].comments)

        dialogView.btnDone.setOnClickListener {
            if(dialogView.etComment.text.toString().isEmpty()){
                ToastHelper.displayInfo(R.string.str_please_enter_comment)
                return@setOnClickListener
            }
            dialogEditComment.dismiss()
            callEditComment(position,dialogView.etComment.text.toString(),dialogView.rbCommentRate.rating)
        }
        builder.setView(dialogView)
        dialogEditComment = builder.create()
        dialogEditComment.show()
    }

    private fun callEditComment(position:Int,comment:String,rate:Float){
        Utility.hideKeyboard(this, btnAdd)
        val jsonObjectParam = JsonObject()
        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
        jsonObjectParam.addProperty(
            WebFields.USERNAME,
            if (LoginHelper.instance.userData.teacherEmailAddress.isEmpty()) ApplicationConstants.API_DEFAULT_USERNAME else LoginHelper.instance.userData.teacherEmailAddress
        )
        jsonObjectParam.addProperty(
            WebFields.PASSWORD,
            if (LoginHelper.instance.userData.password.isEmpty()) ApplicationConstants.API_DEFAULT_PASSWORD else LoginHelper.instance.userData.password
        )
        jsonObjectParam.addProperty(WebFields.TRANS_TYPE_ID, "1")
        jsonObjectParam.addProperty(WebFields.TYPE_ID, commentsModels[position].typeId)
        jsonObjectParam.addProperty(WebFields.COMMENT_TEXT, comment)
        jsonObjectParam.addProperty(WebFields.RATING, rate)
        jsonObjectParam.addProperty(WebFields.REF_ID, homeListModel.id.toString())
        jsonObjectParam.addProperty(WebFields.REF_TYPE_ID, "1")
        jsonObjectParam.addProperty(
            WebFields.CREATED_BY,
            if (LoginHelper.instance.userData.id == 0) ApplicationConstants.API_DEFAULT_USERID else LoginHelper.instance.userData.id.toString()
        )
        jsonObjectParam.addProperty(WebFields.IS_EDITED, "0")
        jsonObjectParam.addProperty(WebFields.EDITED_BY, "0")
        jsonObjectParam.addProperty(WebFields.IS_DELETED, "0")
        jsonObjectParam.addProperty(WebFields.DELETED_BY, "0")
        jsonObjectParam.addProperty(WebFields.TRANS_ID,commentsModels[position].transId)

        ApiCallingHelper().callPostApi(
            this, WebLinks.GET_TRANSACTION_LOG, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val c = commentsModels[position]
                    commentsModels[position] = c
                    c.comments = comment
                    c.rating= rate.toString()
                    commentsAdapter.notifyDataSetChanged()
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            }
        )
    }

    override fun onBackPressed() {
        Utility.killActivity(this)
    }
}
