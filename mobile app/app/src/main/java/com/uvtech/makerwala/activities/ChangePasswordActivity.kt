package com.uvtech.makerwala.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.R
import com.uvtech.makerwala.api.ApiCallingHelper
import com.uvtech.makerwala.api.ApiCallingInterface
import com.uvtech.makerwala.api.WebFields
import com.uvtech.makerwala.api.WebLinks
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.helpers.ToastHelper
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar.*

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        setSupportActionBar(toolbar)
        tvTitle.text = resources.getString(R.string.str_change_password)
        llLeft.setOnClickListener { onBackPressed() }
        btnSubmit.setOnClickListener { onClickSubmit() }
        ApplicationLoader.getInstance().callActivityLog(WebFields.INFORMATION,"Change Password")
    }

    private fun checkValidation(): Boolean {
        if (etCurrentPassword.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_current_password)
            return false
        }
        if (etNewPassword.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_new_password)
            return false
        }
        if (etNewPassword.text.toString() != etConfirmNewPassword.text.toString()) {
            ToastHelper.displayDialog(this, R.string.str_new_password_and_confirm_password_must_be_same)
            return false
        }
        return true
    }

    private fun onClickSubmit() {
        if (checkValidation()) {
            val jsonObjectParam = JsonObject()
            jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)
            jsonObjectParam.addProperty(WebFields.CURRENT_PASSWORD, etCurrentPassword.text.toString())
            jsonObjectParam.addProperty(WebFields.NEW_PASSWORD, etNewPassword.text.toString())
            jsonObjectParam.addProperty(WebFields.USER_ID, LoginHelper.instance.userData.id)
            jsonObjectParam.addProperty(
                WebFields.USERNAME,
                if (LoginHelper.instance.userData.teacherEmailAddress.isEmpty()) ApplicationConstants.API_DEFAULT_USERNAME else LoginHelper.instance.userData.teacherEmailAddress
            )
            jsonObjectParam.addProperty(
                WebFields.PASSWORD,
                if (LoginHelper.instance.userData.password.isEmpty()) ApplicationConstants.API_DEFAULT_PASSWORD else LoginHelper.instance.userData.password
            )

            ApiCallingHelper().callPostApi(this, WebLinks.CHANGE_PASSWORD, jsonObjectParam,
                    object : ApiCallingInterface {
                        override fun onSuccess(response: String, message: String) {
                            ToastHelper.displayInfo(message)
                            onBackPressed()
                        }

                        override fun onFailure(errorMessage: String) {
                            ToastHelper.displayInfo(errorMessage)
                        }
                    })
        }
    }

    override fun onBackPressed() {
        Utility.killActivity(this)
    }
}
