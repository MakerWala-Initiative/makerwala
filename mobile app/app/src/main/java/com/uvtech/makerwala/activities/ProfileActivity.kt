package com.uvtech.makerwala.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.R
import com.uvtech.makerwala.adapters.SelectClassesAdapter
import com.uvtech.makerwala.api.ApiCallingHelper
import com.uvtech.makerwala.api.ApiCallingInterface
import com.uvtech.makerwala.api.WebFields
import com.uvtech.makerwala.api.WebLinks
import com.uvtech.makerwala.cropimage.CropImage
import com.uvtech.makerwala.cropimage.CropImageView
import com.uvtech.makerwala.helpers.*
import com.uvtech.makerwala.models.CountryModel
import com.uvtech.makerwala.utilities.Utility
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.dialog_list_with_button.view.*
import kotlinx.android.synthetic.main.dialog_title.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private var selectedImagePath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        tvTitle.text = resources.getString(R.string.str_profile)
        llLeft.setOnClickListener { onBackPressed() }
        btnSubmit.setOnClickListener { onClickSubmit() }

        tvCountry.setOnClickListener { onClickCountry() }
        tvState.setOnClickListener { onClickState() }
        tvCity.setOnClickListener { onClickCity() }

        tvSchool.setOnClickListener { onClickSchool() }
        tvClass.setOnClickListener { onClickClass() }
        tvSubject.setOnClickListener { onClickSubject() }
        tvBlock.setOnClickListener { onClickBlock() }
        flProfile.setOnClickListener { openDialogProfileOption() }
        setData()
        ApplicationLoader.getInstance().callActivityLog(WebFields.INFORMATION,"Profile")
    }

    private fun setData() {
        try {
            etFirstName.setText(LoginHelper.instance.userData.teacherFirstName)
            etLastName.setText(LoginHelper.instance.userData.teacherLastName)
            etEmailAddress.setText(LoginHelper.instance.userData.teacherEmailAddress)
            etMobileNumber.setText(LoginHelper.instance.userData.teacherMobile)
            etAddress.setText(LoginHelper.instance.userData.teacherAddress)

            tvCountry.text = LoginHelper.instance.userData.countryName
            countrySelectedId = LoginHelper.instance.userData.countryId.toInt()
            tvState.text = LoginHelper.instance.userData.stateName
            stateSelectedId = LoginHelper.instance.userData.stateId.toInt()
            tvCity.text = LoginHelper.instance.userData.cityName
            citySelectedId = LoginHelper.instance.userData.cityId.toInt()
            tvSchool.text = LoginHelper.instance.userData.schoolName
            schoolSelectedId = LoginHelper.instance.userData.schoolId.toInt()
            tvClass.text = LoginHelper.instance.userData.countryName
            tvSubject.text = LoginHelper.instance.userData.countryName
            tvBlock.text = LoginHelper.instance.userData.blockName
            blockSelectedId = LoginHelper.instance.userData.blockId.toInt()

            val classIds = LoginHelper.instance.userData.classIds
            val classNames = LoginHelper.instance.userData.classNames
            val subjectIds = LoginHelper.instance.userData.subjectIds
            val subjectNames = LoginHelper.instance.userData.subjectNames

            classSelectedIds = JsonArray()
            for (i in 0 until classIds.size) {
                classSelectedIds.add(classIds[i])
            }
            subjectSelectedIds = JsonArray()
            for (i in 0 until subjectIds.size) {
                subjectSelectedIds.add(subjectIds[i])
            }

            val stringClass = StringBuffer()

            for (i in 0 until classNames.size) {
                if (stringClass.toString().isNotEmpty())
                    stringClass.append(", ")
                stringClass.append(classNames[i])
            }

            tvClass.text = stringClass.toString()

            val stringSubject = StringBuffer()

            for (i in 0 until subjectNames.size) {
                if (stringSubject.toString().isNotEmpty())
                    stringSubject.append(", ")
                stringSubject.append(subjectNames[i])
            }

            tvSubject.text = stringSubject.toString()

            LogHelper.e("asdf," + LoginHelper.instance.userData.imgProfile)
            if (LoginHelper.instance.userData.imgProfile.isNotEmpty()) {
                val decodedString = Base64.decode(LoginHelper.instance.userData.imgProfile, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                Glide.with(this).asBitmap().load(decodedByte)
                    .apply(
                        RequestOptions()
                            .transform(CircleCrop())
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_logo)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .into(ivProfile)
            }
        } catch (e: Exception) {
            LogHelper.e(e.message)
        }
    }

    private fun checkValidation(): Boolean {
        if (etFirstName.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_first_name)
            return false
        }
        if (etLastName.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_last_name)
            return false
        }
        if (etEmailAddress.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_email_address)
            return false
        }
        if (ValidationHelper.isValidEmail(etEmailAddress.text.toString())) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_valid_email_address)
            return false
        }
        if (etMobileNumber.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_mobile_number)
            return false
        }
        if (ValidationHelper.isValidMobileNumber(etMobileNumber.text.toString())) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_valid_mobile_number)
            return false
        }
        if (tvCountry.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_select_country)
            return false
        }
        if (tvState.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_select_state)
            return false
        }
        if (tvCity.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_select_city)
            return false
        }
        return true
    }

    private fun onClickSubmit() {
        if (checkValidation()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
            jsonObject.addProperty(WebFields.TEACHER_ID, LoginHelper.instance.userData.id)
            jsonObject.addProperty(WebFields.BLOCK_ID, blockSelectedId)
            jsonObject.addProperty(WebFields.TEACHER_FIRST_NAME, etFirstName.text.toString())
            jsonObject.addProperty(WebFields.TEACHER_LAST_NAME, etLastName.text.toString())
            jsonObject.addProperty(WebFields.TEACHER_MOBILE, etMobileNumber.text.toString())
            jsonObject.addProperty(WebFields.TEACHER_EMAIL, etEmailAddress.text.toString())
            jsonObject.addProperty(WebFields.TEACHER_ADDRESS, etAddress.text.toString())
            jsonObject.add(WebFields.CLASS_IDS, classSelectedIds)
            jsonObject.add(WebFields.SUBJECT_IDS, subjectSelectedIds)
            jsonObject.addProperty(WebFields.COUNTRY_ID, countrySelectedId)
            jsonObject.addProperty(WebFields.STATE_ID, stateSelectedId)
            jsonObject.addProperty(WebFields.CITY_ID, citySelectedId)
            jsonObject.addProperty(WebFields.REMARKS, "")
            jsonObject.addProperty(WebFields.IS_BLOCK_ADMIN, false)
            jsonObject.addProperty(WebFields.USER_ROLE_ID, 3)
            jsonObject.addProperty(WebFields.IS_ACTIVE, true)
            jsonObject.addProperty(WebFields.SCHOOL_ID, schoolSelectedId)
            jsonObject.addProperty(WebFields.CREATED_BY, 1)
            jsonObject.addProperty(WebFields.UPDATED_BY, 1)
            jsonObject.addProperty(WebFields.GMAIL_ID, 0)
            if (encodedImage != "")
                jsonObject.addProperty(WebFields.IMG_PROFILE, if (encodedImage == "1") "" else encodedImage)

            ApiCallingHelper().callPostApi(this, WebLinks.REGISTRATION, jsonObject,
                object : ApiCallingInterface {
                    override fun onSuccess(response: String, message: String) {
                        ToastHelper.displayInfo(message)

                        val u = LoginHelper.instance.userData
                        u.blockId = blockSelectedId
                        u.blockName = tvBlock.text.toString()
                        u.teacherMobile = etMobileNumber.text.toString()
                        u.teacherEmailAddress = etEmailAddress.text.toString()
                        u.teacherAddress = etAddress.text.toString()
                        u.countryId = countrySelectedId
                        u.countryName = tvCountry.text.toString()
                        u.stateId = stateSelectedId
                        u.stateName = tvState.text.toString()
                        u.cityId = citySelectedId
                        u.cityName = tvCity.text.toString()
                        u.schoolId = schoolSelectedId
                        u.schoolName = tvSchool.text.toString()
                        u.teacherFirstName = etFirstName.text.toString()
                        u.teacherLastName = etLastName.text.toString()
                        u.imgProfile = if (encodedImage == "1") "" else encodedImage


                        val cIds = ArrayList<Int>()
                        val cNames = ArrayList<String>()
                        val sIds = ArrayList<Int>()
                        val sNames = ArrayList<String>()

                        for (i in 0 until classIds.length()) {
                            cIds.add(classIds.getInt(i))
                            cNames.add(classNames.getString(i))
                        }
                        for (i in 0 until subjectIds.length()) {
                            sIds.add(classIds.getInt(i))
                            sNames.add(classNames.getString(i))
                        }

                        u.classIds = cIds
                        u.classNames = cNames
                        u.subjectIds = sIds
                        u.subjectNames = sNames

                        LoginHelper.instance.userData = u
                        Utility.callNewActivity(this@ProfileActivity, MainActivity::class.java)
                    }

                    override fun onFailure(errorMessage: String) {
                        ToastHelper.displayInfo(errorMessage)
                    }
                })
        }
    }

    var countryNames = arrayOf<String?>()
    var countryModels = ArrayList<CountryModel>()
    private var countrySelectedId = 0

    private fun onClickCountry() {
        if (countryModels.size > 0) {
            openDialogCountry()
        } else {
            callGetCountry()
        }
    }

    private fun openDialogCountry() {
        val builder = AlertDialog.Builder(this)
        builder.setItems(countryNames) { _, which ->
            countrySelectedId = countryModels[which].id
            tvCountry.text = countryModels[which].title
            tvState.text = ""
            stateNames = arrayOf("")
            stateModels = ArrayList()
            stateSelectedId = 0
            tvCity.text = ""
            cityNames = arrayOf("")
            cityModels = ArrayList()
            citySelectedId = 0
        }
        val inflater = this.layoutInflater
        val dialogTitle = inflater.inflate(R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(R.string.str_select_country)
        builder.setCustomTitle(dialogTitle)
        builder.create().show()
    }

    private fun callGetCountry() {
        countryNames = arrayOf("")
        countryModels = ArrayList()
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

        ApiCallingHelper().callPostApi(this, WebLinks.GET_COUNTRY, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val jsonObject = JSONObject(response)
                    val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                    countryNames = arrayOfNulls(jsonArrayData.length())
                    for (i in 0 until jsonArrayData.length()) {
                        val json = jsonArrayData.getJSONObject(i)
                        countryNames[i] = json.getString(WebFields.COUNTRY_NAME)
                        countryModels.add(
                            CountryModel(
                                JsonHelper.getInt(json, WebFields.COUNTRY_ID),
                                JsonHelper.getString(json, WebFields.COUNTRY_NAME)
                            )
                        )
                    }
                    openDialogCountry()
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
    }

    var stateNames = arrayOf<String?>()
    var stateModels = ArrayList<CountryModel>()
    private var stateSelectedId = 0

    private fun onClickState() {
        if (countrySelectedId == 0) {
            ToastHelper.displayDialog(this, R.string.str_select_country)
            return
        }
        if (stateModels.size > 0) {
            openDialogState()
        } else {
            callGetState()
        }
    }

    private fun openDialogState() {
        val builder = AlertDialog.Builder(this)
        builder.setItems(stateNames) { _, which ->
            stateSelectedId = stateModels[which].id
            tvState.text = stateModels[which].title
            tvCity.text = ""
            cityNames = arrayOf("")
            cityModels = ArrayList()
            citySelectedId = 0
        }
        val inflater = this.layoutInflater
        val dialogTitle = inflater.inflate(R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(R.string.str_select_state)
        builder.setCustomTitle(dialogTitle)
        builder.create().show()
    }

    private fun callGetState() {
        stateNames = arrayOf("")
        stateModels = ArrayList()
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
        jsonObjectParam.addProperty(WebFields.COUNTRY_ID, countrySelectedId)

        ApiCallingHelper().callPostApi(this, WebLinks.GET_STATE, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val jsonObject = JSONObject(response)
                    val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                    stateNames = arrayOfNulls(jsonArrayData.length())
                    for (i in 0 until jsonArrayData.length()) {
                        val json = jsonArrayData.getJSONObject(i)
                        stateNames[i] = json.getString(WebFields.STATE_NAME)
                        stateModels.add(
                            CountryModel(
                                JsonHelper.getInt(json, WebFields.STATE_ID),
                                JsonHelper.getString(json, WebFields.STATE_NAME)
                            )
                        )
                    }
                    openDialogState()
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
    }

    var cityNames = arrayOf<String?>()
    var cityModels = ArrayList<CountryModel>()
    private var citySelectedId = 0

    private fun onClickCity() {
        if (stateSelectedId == 0) {
            ToastHelper.displayDialog(this, R.string.str_select_state)
            return
        }
        if (cityModels.size > 0) {
            openDialogCity()
        } else {
            callGetCity()
        }
    }

    private fun openDialogCity() {
        val builder = AlertDialog.Builder(this)
        builder.setItems(cityNames) { _, which ->
            citySelectedId = cityModels[which].id
            tvCity.text = cityModels[which].title
        }
        val inflater = this.layoutInflater
        val dialogTitle = inflater.inflate(R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(R.string.str_select_city)
        builder.setCustomTitle(dialogTitle)
        builder.create().show()
    }

    private fun callGetCity() {
        cityNames = arrayOf("")
        cityModels = ArrayList()
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
        jsonObjectParam.addProperty(WebFields.STATE_ID, stateSelectedId)

        ApiCallingHelper().callPostApi(this, WebLinks.GET_CITY, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val jsonObject = JSONObject(response)
                    val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                    cityNames = arrayOfNulls(jsonArrayData.length())
                    for (i in 0 until jsonArrayData.length()) {
                        val json = jsonArrayData.getJSONObject(i)
                        cityNames[i] = json.getString(WebFields.CITY_NAME)
                        cityModels.add(
                            CountryModel(
                                JsonHelper.getInt(json, WebFields.CITY_ID),
                                JsonHelper.getString(json, WebFields.CITY_NAME)
                            )
                        )
                    }
                    openDialogCity()
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
    }

    var schoolNames = arrayOf<String?>()
    var schoolModels = ArrayList<CountryModel>()
    private var schoolSelectedId = 0

    private fun onClickSchool() {
        if (schoolModels.size > 0) {
            openDialogSchool()
        } else {
            callGetSchool()
        }
    }

    private fun openDialogSchool() {
        val builder = AlertDialog.Builder(this)
        builder.setItems(schoolNames) { _, which ->
            schoolSelectedId = schoolModels[which].id
            tvSchool.text = schoolModels[which].title
        }
        val inflater = this.layoutInflater
        val dialogTitle = inflater.inflate(R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(R.string.str_select_school)
        builder.setCustomTitle(dialogTitle)
        builder.create().show()
    }

    private fun callGetSchool() {
        schoolNames = arrayOf("")
        schoolModels = ArrayList()
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

        ApiCallingHelper().callPostApi(this, WebLinks.GET_SCHOOL, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val jsonObject = JSONObject(response)
                    val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                    schoolNames = arrayOfNulls(jsonArrayData.length())
                    for (i in 0 until jsonArrayData.length()) {
                        val json = jsonArrayData.getJSONObject(i)
                        schoolNames[i] = json.getString(WebFields.SCHOOL_NAME)
                        schoolModels.add(
                            CountryModel(
                                JsonHelper.getInt(json, WebFields.SCHOOL_ID),
                                JsonHelper.getString(json, WebFields.SCHOOL_NAME)
                            )
                        )
                    }
                    openDialogSchool()
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
    }

    var classModels = ArrayList<CountryModel>()
    private var classSelectedIds: JsonArray = JsonArray()
    private var classIds: JSONArray = JSONArray()
    private var classNames: JSONArray = JSONArray()
    private lateinit var dialogClass: AlertDialog
    private lateinit var selectClassesAdapter: SelectClassesAdapter

    private fun onClickClass() {
        if (classModels.size > 0) {
            openDialogClass()
        } else {
            callGetClass()
        }
    }

    private fun openDialogClass() {
        val builder = AlertDialog.Builder(this)
        val dialogTitle = View.inflate(this, R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(R.string.str_select_class)
        builder.setCustomTitle(dialogTitle)

        val dialogView = View.inflate(this, R.layout.dialog_list_with_button, null)

        selectClassesAdapter = SelectClassesAdapter(this, classModels)
        dialogView.recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
        itemDecor.setDrawable(resources.getDrawable(R.drawable.list_bar))
        dialogView.recyclerView.addItemDecoration(itemDecor)
        dialogView.recyclerView.adapter = selectClassesAdapter

        dialogView.btnDone.setOnClickListener {
            dialogClass.dismiss()
            classSelectedIds = selectClassesAdapter.getSelectedIds()
            classIds = selectClassesAdapter.getIds()
            classNames = selectClassesAdapter.getNames()
            tvClass.text = selectClassesAdapter.getSelectedNames()
        }
        builder.setView(dialogView)
        dialogClass = builder.create()
        if (classModels.size > 0)
            dialogClass.show()
    }

    private fun callGetClass() {
        classModels = ArrayList()
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

        ApiCallingHelper().callPostApi(this, WebLinks.GET_CLASS, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val jsonObject = JSONObject(response)
                    val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                    for (i in 0 until jsonArrayData.length()) {
                        val json = jsonArrayData.getJSONObject(i)
                        classModels.add(
                            CountryModel(
                                JsonHelper.getInt(json, WebFields.CLASS_ID),
                                JsonHelper.getString(json, WebFields.CLASS_LEVEL_NAME)
                            )
                        )
                    }
                    openDialogClass()
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
    }

    var subjectModels = ArrayList<CountryModel>()
    private var subjectSelectedIds: JsonArray = JsonArray()
    private var subjectIds: JSONArray = JSONArray()
    private var subjectNames: JSONArray = JSONArray()
    private lateinit var dialogSubject: AlertDialog
    private lateinit var selectSubjectsAdapter: SelectClassesAdapter

    private fun onClickSubject() {
        if (classSelectedIds.size() == 0) {
            ToastHelper.displayDialog(this, R.string.str_select_class_level)
            return
        }
        if (subjectModels.size > 0) {
            openDialogSubject()
        } else {
            callGetSubject()
        }
    }

    private fun openDialogSubject() {
        val builder = AlertDialog.Builder(this)
        val dialogTitle = View.inflate(this, R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(R.string.str_select_subject)
        builder.setCustomTitle(dialogTitle)

        val dialogView = View.inflate(this, R.layout.dialog_list_with_button, null)

        selectSubjectsAdapter = SelectClassesAdapter(this, subjectModels)
        dialogView.recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
        itemDecor.setDrawable(resources.getDrawable(R.drawable.list_bar))
        dialogView.recyclerView.addItemDecoration(itemDecor)
        dialogView.recyclerView.adapter = selectSubjectsAdapter

        dialogView.btnDone.setOnClickListener {
            dialogSubject.dismiss()
            subjectSelectedIds = selectSubjectsAdapter.getSelectedIds()
            subjectIds = selectSubjectsAdapter.getIds()
            subjectNames = selectSubjectsAdapter.getNames()
            tvSubject.text = selectSubjectsAdapter.getSelectedNames()
        }
        builder.setView(dialogView)
        dialogSubject = builder.create()
        if (subjectModels.size > 0)
            dialogSubject.show()
    }

    private fun callGetSubject() {
        subjectModels = ArrayList()
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
        jsonObjectParam.add(WebFields.CLASS_ID, classSelectedIds)

        ApiCallingHelper().callPostApi(this, WebLinks.GET_SUBJECT, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val jsonObject = JSONObject(response)
                    val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                    for (i in 0 until jsonArrayData.length()) {
                        val json = jsonArrayData.getJSONObject(i)
                        subjectModels.add(
                            CountryModel(
                                JsonHelper.getInt(json, WebFields.SUBJECT_ID),
                                JsonHelper.getString(json, WebFields.SUBJECT_NAME)
                            )
                        )
                    }
                    openDialogSubject()
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
    }

    var blockNames = arrayOf<String?>()
    var blockModels = ArrayList<CountryModel>()
    private var blockSelectedId = 0

    private fun onClickBlock() {
        if (blockModels.size > 0) {
            openDialogBlock()
        } else {
            callGetBlock()
        }
    }

    private fun openDialogBlock() {
        val builder = AlertDialog.Builder(this)
        builder.setItems(blockNames) { _, which ->
            blockSelectedId = blockModels[which].id
            tvBlock.text = blockModels[which].title
        }
        val inflater = this.layoutInflater
        val dialogTitle = inflater.inflate(R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(
            R.string.str_select_block
        )
        builder.setCustomTitle(dialogTitle)
        builder.create().show()
    }

    private fun callGetBlock() {
        blockNames = arrayOf("")
        blockModels = ArrayList()
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

        ApiCallingHelper().callPostApi(this, WebLinks.GET_BLOCK, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    val jsonObject = JSONObject(response)
                    val jsonArrayData = jsonObject.getJSONArray(WebFields.DATA)

                    blockNames = arrayOfNulls(jsonArrayData.length())
                    for (i in 0 until jsonArrayData.length()) {
                        val json = jsonArrayData.getJSONObject(i)
                        blockNames[i] = json.getString(WebFields.BLOCK_NAME)
                        blockModels.add(
                            CountryModel(
                                JsonHelper.getInt(json, WebFields.BLOCK_ID),
                                JsonHelper.getString(json, WebFields.BLOCK_NAME)
                            )
                        )
                    }
                    openDialogBlock()
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
    }

    private fun openDialogProfileOption() {
        val builder = AlertDialog.Builder(this)
        builder.setItems(R.array.array_profile_option) { _, which ->
            when (which) {
                0 -> onClickProfileUpdate()
                1 -> {
                    encodedImage = "1"
                    Glide.with(this).asBitmap().load(R.drawable.ic_logo)
                        .apply(
                            RequestOptions()
                                .transform(CircleCrop())
                                .placeholder(R.drawable.ic_logo)
                                .error(R.drawable.ic_logo)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                        )
                        .into(ivProfile)
                }
            }
        }
        val inflater = this.layoutInflater
        val dialogTitle = inflater.inflate(R.layout.dialog_title, null)
        dialogTitle.tvTitle.setText(
            R.string.str_select_block
        )
        builder.setCustomTitle(dialogTitle)
        builder.create().show()
    }

    private fun onClickProfileUpdate() {
        checkStoragePermissions()
    }

    private fun checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                ApplicationConstants.REQUEST_PERMISSIONS_STORAGE
            )
            return
        }
        CropImage.startPickImageActivity(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE -> if (permissions.size == 1)
                CropImage.activity(Uri.parse(selectedImagePath))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            ApplicationConstants.REQUEST_PERMISSIONS_STORAGE -> checkStoragePermissions()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                selectedImagePath = CropImage.getPickImageResultUri(this, data).toString()
                if (CropImage.isReadExternalStoragePermissionsRequired(this, Uri.parse(selectedImagePath))) {
                    if (Build.VERSION.SDK_INT > 22) {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
                        )
                    }
                } else {
                    CropImage.activity(Uri.parse(selectedImagePath))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri

                    try {
                        selectedImagePath = resultUri.path

                        if (File(selectedImagePath).length() > 1048576)
                            selectedImagePath = Compressor(this)
                                .compressToFile(File(selectedImagePath))
                                .absolutePath
                        Glide.with(this).asBitmap().load(selectedImagePath)
                            .apply(
                                RequestOptions()
                                    .transform(CircleCrop())
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                            )
                            .into(ivProfile)

                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, resultUri);
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val byteArrayImage = byteArrayOutputStream.toByteArray()
                        encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
                    } catch (e: Exception) {
                        LogHelper.e(e.message)
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    LogHelper.e("onActivityResult: Error in cropping")
                }
            }
        }
    }

    var encodedImage = ""

    override fun onBackPressed() {
        Utility.killActivity(this)
    }
}
