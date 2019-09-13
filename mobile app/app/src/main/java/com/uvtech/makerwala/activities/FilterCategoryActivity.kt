package com.uvtech.makerwala.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.R
import com.uvtech.makerwala.adapters.FilterCategoryAdapter
import com.uvtech.makerwala.api.ApiCallingHelper
import com.uvtech.makerwala.api.ApiCallingInterface
import com.uvtech.makerwala.api.WebFields
import com.uvtech.makerwala.api.WebLinks
import com.uvtech.makerwala.helpers.JsonHelper
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.helpers.ToastHelper
import com.uvtech.makerwala.interfaces.OnItemClickListener
import com.uvtech.makerwala.models.FilterCategoryModel
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_filter_category.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject

class FilterCategoryActivity : AppCompatActivity(), OnItemClickListener {

    companion object {
        var filterCategoryActivity: FilterCategoryActivity? = null
    }

    private var filterCategoryModels = ArrayList<FilterCategoryModel>()
    private lateinit var filterCategoryAdapter: FilterCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_category)
        setSupportActionBar(toolbar)
        filterCategoryActivity = this

        tvTitle.text = resources.getString(R.string.str_filter_class_level)
        llLeft.setOnClickListener { onBackPressed() }

        ApplicationLoader.getInstance().jsonArrayFilterCategory = JsonArray()
        ApplicationLoader.getInstance().jsonArrayFilterSubCategory = JsonArray()

        btnClear.setOnClickListener {
            ApplicationLoader.getInstance().jsonArrayFilterCategory = JsonArray()
            ApplicationLoader.getInstance().jsonArrayFilterSubCategory = JsonArray()
            onBackPressed()
        }
        btnNext.setOnClickListener {
            if (filterCategoryAdapter.getSelectedIds().size() != 0) {
                ApplicationLoader.getInstance().jsonArrayFilterCategory = filterCategoryAdapter.getSelectedIds()
                Utility.callActivity(this, FilterSubCategoryActivity::class.java)
            } else {
                ToastHelper.displayDialog(this, R.string.str_select_at_least_one_option)
            }
        }

        filterCategoryAdapter = FilterCategoryAdapter(this, filterCategoryModels)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = filterCategoryAdapter

        callGetClass()
        ApplicationLoader.getInstance().callActivityLog(WebFields.INFORMATION,"Filter - Class")
    }

    private fun callGetClass() {
        val jsonObjectParam = JsonObject()
        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
        jsonObjectParam.addProperty(WebFields.USERNAME, if(LoginHelper.instance.userData.teacherEmailAddress.isEmpty()) ApplicationConstants.API_DEFAULT_USERNAME else LoginHelper.instance.userData.teacherEmailAddress)
        jsonObjectParam.addProperty(WebFields.PASSWORD, if(LoginHelper.instance.userData.password.isEmpty()) ApplicationConstants.API_DEFAULT_PASSWORD else LoginHelper.instance.userData.password)

        ApiCallingHelper().callPostApi(this, WebLinks.GET_CLASS, jsonObjectParam,
                object : ApiCallingInterface {
                    override fun onSuccess(response: String, message: String) {
                        try {
                            val jsonObject = JSONObject(response)
                            val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)
                            filterCategoryModels.add(
                                FilterCategoryModel(
                                    0,
                                    "All"
                                )
                            )
                            for (i in 0 until jsonArrayData.length()) {
                                val json = jsonArrayData.getJSONObject(i)
                                filterCategoryModels.add(
                                    FilterCategoryModel(
                                        JsonHelper.getInt(json, WebFields.CLASS_ID),
                                        JsonHelper.getString(json, WebFields.CLASS_LEVEL_NAME)
                                    )
                                )
                            }
                            filterCategoryAdapter.notifyDataSetChanged()
                        }catch (e:Exception){
                            ApplicationLoader.getInstance().callActivityLog(WebFields.EXCEPTION, "Filter - Class - " + e.message)
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

    override fun onDestroy() {
        super.onDestroy()
        filterCategoryActivity = null
    }
}
