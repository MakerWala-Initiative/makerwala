package com.uvtech.makerwala.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.ApplicationConstants.ON_ACTIVITY_RESULT_VIDEO_DETAIL_TO_DOWNLOAD
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.R
import com.uvtech.makerwala.activities.FilterCategoryActivity
import com.uvtech.makerwala.activities.VideoDetailsActivity
import com.uvtech.makerwala.adapters.HomeListAdapter
import com.uvtech.makerwala.api.ApiCallingHelper
import com.uvtech.makerwala.api.ApiCallingInterface
import com.uvtech.makerwala.api.WebFields
import com.uvtech.makerwala.api.WebLinks
import com.uvtech.makerwala.helpers.JsonHelper
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.helpers.ToastHelper
import com.uvtech.makerwala.interfaces.OnItemClickListener
import com.uvtech.makerwala.models.HomeListModel
import com.uvtech.makerwala.utilities.Utility
import com.uvtech.makerwala.views.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.json.JSONObject

class FragmentHome : Fragment(), OnItemClickListener {

    private var homeListModels = ArrayList<HomeListModel>()
    private lateinit var homeListAdapter: HomeListAdapter

    private var isLoading = false
    private var isLoader = true
    private var isLastPage = false
    private var TOTAL_PAGES = 0
    private var currentPage = PAGE_START

    companion object {
        private const val PAGE_START = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeListAdapter = HomeListAdapter(context, homeListModels, this)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tvTitle.text = resources.getString(R.string.str_app_name)
        view.llLeft.visibility = View.INVISIBLE

        view.fabFilter.setOnClickListener { Utility.callActivity(context!!, FilterCategoryActivity::class.java) }

        val layoutParams = LinearLayoutManager(context)
        view.recyclerView!!.layoutManager = layoutParams
        view.recyclerView!!.adapter = homeListAdapter

        view.recyclerView!!.addOnScrollListener(object : PaginationScrollListener(layoutParams) {
            override fun getTotalPageCount(): Int {
                return this@FragmentHome.TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return this@FragmentHome.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@FragmentHome.isLoading
            }

            override fun loadMoreItems() {
                this@FragmentHome.isLoading = true
                this@FragmentHome.isLastPage = false
                this@FragmentHome.isLoader = false
                currentPage += 1

                Handler().postDelayed({
                    sendRequestGetVideoList()
                }, 1000)
            }
        })

        view.swipeToRefresh.setOnRefreshListener {
            view.swipeToRefresh.isRefreshing = false
            homeListModels.clear()
            homeListAdapter.notifyDataSetChanged()
            callGetVideoList()
        }

        callGetVideoList()
    }

    private fun callGetVideoList() {
        homeListModels.clear()
        homeListAdapter.notifyDataSetChanged()
        currentPage = PAGE_START
        isLoading = false
        isLoader = true
        isLastPage = false
        sendRequestGetVideoList()
    }

    private fun sendRequestGetVideoList() {
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
        jsonObjectParam.addProperty(WebFields.PAGE_INDEX, currentPage)
        jsonObjectParam.add(WebFields.CLASS_ID, ApplicationLoader.getInstance().jsonArrayFilterCategory)
        jsonObjectParam.add(WebFields.SUBJECT_ID, ApplicationLoader.getInstance().jsonArrayFilterSubCategory)

        ApiCallingHelper().callPostApi(
            context, WebLinks.GET_VIDEO_LIST, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    try {
                        isLoading = false
                        val jsonObject = JSONObject(response)
                        val jsonObjectData = jsonObject.getJSONObject(WebFields.DATA)
                        val jsonArrayVideoList = jsonObjectData.getJSONArray(WebFields.VIDEO_LISTS)

                        try {
                            homeListAdapter.removeLoadingFooter()
                            for (i in 0 until jsonArrayVideoList.length()) {
                                val json = jsonArrayVideoList.getJSONObject(i)
                                val s = HomeListModel(
                                    JsonHelper.getInt(json, WebFields.VIDEO_ID),
                                    JsonHelper.getString(json, WebFields.YOUTUBE_LINK),
                                    JsonHelper.getString(json, WebFields.VIDEO_TITLE),
                                    JsonHelper.getString(json, WebFields.PUBLIC_DETAIL),
                                    JsonHelper.getString(json, WebFields.PRIVATE_DETAIL),
                                    JsonHelper.getDouble(json, WebFields.RATING).toFloat(),
                                    JsonHelper.getBoolean(json, WebFields.IS_PRIVATE)
                                )

//                                val sanitizer = UrlQuerySanitizer(s.videoId)
//                                s.videoId = sanitizer.getValue("v")

//                                val uri = Uri.parse(s.videoId)
//                                s.videoId = uri.getQueryParameter("v")!!

                                homeListModels.add(s)
                            }
                        } catch (e: Exception) {
                            LogHelper.e(e.message)
                        }

                        homeListAdapter.notifyDataSetChanged()

                        if (currentPage == 1)
                            TOTAL_PAGES = jsonObjectData.getInt(WebFields.TOTAL_RECORDS)
                        if (TOTAL_PAGES != homeListModels.size) {
                            isLastPage = false
                            homeListAdapter.addLoadingFooter()
                        } else {
                            isLastPage = true
                            homeListAdapter.removeLoadingFooter()
                        }
                    } catch (e: Exception) {
                        ApplicationLoader.getInstance().callActivityLog(WebFields.EXCEPTION, "Home - " + e.message)
                    }
                    //JsonHelper.getString(json, WebFields.YOUTUBE_LINK)
                }

                override fun onFailure(errorMessage: String) {
                    isLastPage = true
                    homeListAdapter.removeLoadingFooter()
                    ToastHelper.displayInfo(errorMessage)
                }
            }, isLoader
        )
    }

    override fun onItemClick(position: Int) {
        ApplicationLoader.getInstance().homeListModel = homeListModels[position]
        Utility.callActivityForResult(
            (context!! as AppCompatActivity),
            VideoDetailsActivity::class.java,
            ON_ACTIVITY_RESULT_VIDEO_DETAIL_TO_DOWNLOAD
        )
    }
}
