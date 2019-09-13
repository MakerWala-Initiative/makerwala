package com.uvtech.makerwala.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.R
import com.uvtech.makerwala.adapters.FilterSubCategoryAdapter
import com.uvtech.makerwala.api.ApiCallingHelper
import com.uvtech.makerwala.api.ApiCallingInterface
import com.uvtech.makerwala.api.WebFields
import com.uvtech.makerwala.api.WebLinks
import com.uvtech.makerwala.helpers.JsonHelper
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.helpers.ToastHelper
import com.uvtech.makerwala.interfaces.OnItemClickListener
import com.uvtech.makerwala.models.FilterSubCategoryModel
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_filter_subcategory.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import java.lang.Exception

class FilterSubCategoryActivity : AppCompatActivity(), OnItemClickListener {

    private var filterSubCategoryModels = ArrayList<FilterSubCategoryModel>()
    private lateinit var filterSubCategoryAdapter: FilterSubCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_subcategory)
        setSupportActionBar(toolbar)
        tvTitle.text = resources.getString(R.string.str_filter_subject_level)
        llLeft.setOnClickListener { onBackPressed() }

        btnApply.setOnClickListener {
            if (filterSubCategoryAdapter.getSelectedIds().size() != 0) {
                ApplicationLoader.getInstance().jsonArrayFilterSubCategory = filterSubCategoryAdapter.getSelectedIds()
                FilterCategoryActivity.filterCategoryActivity?.finish()
                onBackPressed()
            } else {
                ToastHelper.displayDialog(this, R.string.str_select_at_least_one_option)
            }
        }

        filterSubCategoryAdapter = FilterSubCategoryAdapter(this, filterSubCategoryModels, this)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = filterSubCategoryAdapter

        callGetSubjects()
        ApplicationLoader.getInstance().callActivityLog(WebFields.INFORMATION, "Filter - Subject")
    }

    private fun callGetSubjects() {
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
        jsonObjectParam.add(WebFields.CLASS_ID, ApplicationLoader.getInstance().jsonArrayFilterCategory)

        ApiCallingHelper().callPostApi(this, WebLinks.GET_SUBJECT, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    try {
                        val jsonObject = JSONObject(response)
                        val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                        filterSubCategoryModels.add(
                            FilterSubCategoryModel(
                                0,
                                "All"
                            )
                        )
                        for (i in 0 until jsonArrayData.length()) {
                            val json = jsonArrayData.getJSONObject(i)
                            filterSubCategoryModels.add(
                                FilterSubCategoryModel(
                                    JsonHelper.getInt(json, WebFields.SUBJECT_ID),
                                    JsonHelper.getString(json, WebFields.SUBJECT_NAME)
                                )
                            )
                        }
                        filterSubCategoryAdapter.notifyDataSetChanged()
                    }catch (e:Exception){
                        ApplicationLoader.getInstance().callActivityLog(WebFields.EXCEPTION, "Filter - Subject - " + e.message)
                    }
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
    }

    override fun onItemClick(position: Int) {

    }

    override fun onBackPressed() {
        Utility.killActivity(this)
    }
}
