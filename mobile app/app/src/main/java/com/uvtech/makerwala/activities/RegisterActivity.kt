package com.uvtech.makerwala.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.R
import com.uvtech.makerwala.adapters.SelectClassesAdapter
import com.uvtech.makerwala.api.ApiCallingHelper
import com.uvtech.makerwala.api.ApiCallingInterface
import com.uvtech.makerwala.api.WebFields
import com.uvtech.makerwala.api.WebLinks
import com.uvtech.makerwala.helpers.*
import com.uvtech.makerwala.models.CountryModel
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_list_with_button.view.*
import kotlinx.android.synthetic.main.dialog_title.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
        tvTitle.text = resources.getString(R.string.str_register)
        llLeft.setOnClickListener { onBackPressed() }
        btnSubmit.setOnClickListener { onClickSubmit() }

        tvCountry.setOnClickListener { onClickCountry() }
        tvState.setOnClickListener { onClickState() }
        tvCity.setOnClickListener { onClickCity() }

        tvSchool.setOnClickListener { onClickSchool() }
        tvClass.setOnClickListener { onClickClass() }
        tvSubject.setOnClickListener { onClickSubject() }
        tvBlock.setOnClickListener { onClickBlock() }

        if (intent.hasExtra(WebFields.TEACHER_EMAIL))
            etEmailAddress.setText(intent.getStringExtra(WebFields.TEACHER_EMAIL))
        if (intent.hasExtra(WebFields.TEACHER_FIRST_NAME))
            etFirstName.setText(intent.getStringExtra(WebFields.TEACHER_FIRST_NAME))
        if (intent.hasExtra(WebFields.TEACHER_LAST_NAME))
            etLastName.setText(intent.getStringExtra(WebFields.TEACHER_LAST_NAME))
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
        if (etPassword.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_password)
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
            jsonObject.addProperty(WebFields.TEACHER_ID, 0)
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
            jsonObject.addProperty(WebFields.PASSWORD, etPassword.text.toString())
            jsonObject.addProperty(WebFields.USER_ROLE_ID, 3)
            jsonObject.addProperty(WebFields.IS_ACTIVE, true)
            jsonObject.addProperty(WebFields.SCHOOL_ID, schoolSelectedId)
            jsonObject.addProperty(WebFields.CREATED_BY, 1)
            jsonObject.addProperty(WebFields.UPDATED_BY, 1)
            jsonObject.addProperty(WebFields.GMAIL_ID, 0)
            jsonObject.addProperty(WebFields.IMG_PROFILE, "")

            ApiCallingHelper().callPostApi(this, WebLinks.REGISTRATION, jsonObject,
                object : ApiCallingInterface {
                    override fun onSuccess(response: String, message: String) {
                        ToastHelper.displayInfo(message)
                        callLogin(etEmailAddress.text.toString(), etPassword.text.toString())
                    }

                    override fun onFailure(errorMessage: String) {
                        ToastHelper.displayInfo(errorMessage)
                    }
                })
        }
    }

    private fun callLogin(email: String, password: String) {
        val jsonObjectParam = JsonObject()
        jsonObjectParam.addProperty(WebFields.USERNAME, email)
        jsonObjectParam.addProperty(WebFields.PASSWORD, password)
        jsonObjectParam.addProperty(WebFields.TYPE, 2)
        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)

        ApiCallingHelper().callPostApi(this, WebLinks.LOGIN, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    var jsonObject = JSONObject(response)
                    jsonObject = jsonObject.getJSONObject(WebFields.DATA)
                    val jsonObjectLogin = jsonObject.getJSONObject(WebFields.LOGIN_DETAIL)

                    val u = User(
                        JsonHelper.getInt(jsonObjectLogin, WebFields.TEACHER_ID),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.BLOCK_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.BLOCK_NAME),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_MOBILE),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_EMAIL),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_ADDRESS),
                        JsonHelper.getString(jsonObjectLogin, WebFields.PASSWORD),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.COUNTRY_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.COUNTRY_NAME),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.STATE_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.STATE_NAME),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.CITY_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.CITY_NAME),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.USER_ROLE_ID),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.SCHOOL_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.SCHOOL_NAME),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_FIRST_NAME),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_LAST_NAME),
                        JsonHelper.getString(jsonObjectLogin, WebFields.IMG_PROFILE)
                    )

                    val jsonArrayClass = jsonObject.getJSONArray(WebFields.TEACHER_CLASS)
                    val jsonArraySubject = jsonObject.getJSONArray(WebFields.TEACHER_SUBJECT)
                    val classIds = ArrayList<Int>()
                    val classNames = ArrayList<String>()
                    val subjectIds = ArrayList<Int>()
                    val subjectNames = ArrayList<String>()

                    for (i in 0 until jsonArrayClass.length()) {
                        val classes = jsonArrayClass.getJSONObject(i)
                        classIds.add(classes.getInt(WebFields.CLASS_LEVEL_ID))
                        classNames.add(classes.getString(WebFields.CLASS_LEVEL_NAME))
                    }
                    for (i in 0 until jsonArraySubject.length()) {
                        val subjects = jsonArraySubject.getJSONObject(i)
                        subjectIds.add(subjects.getInt(WebFields.SUBJECT_ID))
                        subjectNames.add(subjects.getString(WebFields.SUBJECT_NAME))
                    }

                    u.classIds = classIds
                    u.classNames = classNames
                    u.subjectIds = subjectIds
                    u.subjectNames = subjectNames

                    LoginHelper.instance.userData = u
                    Utility.callNewActivity(this@RegisterActivity, MainActivity::class.java)
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                }
            })
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

    override fun onBackPressed() {
        Utility.killActivity(this)
    }
}
